package com.dcep.supergw.manager.https;

import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.EnviromentUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import com.dcep.supergw.manager.https.interceptor.EnvFlagClientInterceptor;
import com.dcepex.trace.support.TraceContext;
import com.dcepex.trace.support.http.HttpclientInterceptor;
import com.dubbo.ldc.ZoneClient;
import com.dubbo.ldc.constant.EnvEnum;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.ExceptionEvent;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.NHttpClientEventHandler;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.protocol.HttpAsyncRequestExecutor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.logging.log4j.core.util.Integers;
import org.springframework.util.Assert;

@Slf4j
@Getter
public class HttpsAsyncClient {

    //HTTPS参数配置
    private final String HTTP = "http";
    private final String HTTPS = "https";
    private final String fileName = EnviromentUtils.get("https.cert.keystore");
    private final String password = EnviromentUtils.get("https.cert.password");
    private final int maxConnectionsPerHost = Integers
        .parseInt(EnviromentUtils.get("https.pool.maxConnectionsPerHost"));
    private final int maxTotalConnections = Integers.parseInt(EnviromentUtils.get("https.pool.maxTotalConnections"));
    private final int connectionRequestTimeout = Integers
        .parseInt(EnviromentUtils.get("https.timeout.connectionRequest"));
    private final int connectionTimeout = Integers.parseInt(EnviromentUtils.get("https.timeout.connection"));
    private final int socketTimeout = Integers.parseInt(EnviromentUtils.get("https.timeout.socket"));
    private final String[] sslProtocols = EnviromentUtils.get("https.protocol.ssl", String[].class, null);
    private final long sleepTime = EnviromentUtils.get("https.evictor.sleepTime", Long.class, 5L);
    private final long maxIdleTime = EnviromentUtils.get("https.evictor.maxIdleTime", Long.class, 5L);

    private DefaultConnectingIOReactor ioReactor;
    private PoolingNHttpClientConnectionManager manager;
    private CloseableHttpAsyncClient client;
    private IdleConnectionEvictor connEvictor;

    public HttpsAsyncClient() {
        try {
            client = HttpAsyncClients.custom()
                .setConnectionManager(getConnectionManager())
                .setDefaultRequestConfig(getRequestConfig())
                // 配合全链路监控修改，增加请求相应的拦截器
                .addInterceptorLast((HttpRequestInterceptor) new HttpclientInterceptor())
                .addInterceptorFirst((HttpResponseInterceptor) new HttpclientInterceptor())
                // 增加环境位相关的拦截器
                .addInterceptorFirst((HttpRequestInterceptor) new EnvFlagClientInterceptor())
                .setEventHandler(getEventHandler())
                .build();

            connEvictor = new IdleConnectionEvictor(manager, sleepTime, maxIdleTime);
            connEvictor.setDaemon(true);
            connEvictor.start();

        } catch (IOReactorException e) {
            log.error("HttpsAsyncClientFactory.init exception: ", e);
            throw new GwException(GwErrorEnum.SYSTEM_CONFIG_ERROR, e);
        }
    }

    private RequestConfig getRequestConfig() {
        return RequestConfig.custom()
            .setConnectionRequestTimeout(connectionRequestTimeout)
            .setConnectTimeout(connectionTimeout)
            .setSocketTimeout(socketTimeout)
            .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
            .setExpectContinueEnabled(Boolean.FALSE)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
            .build();
    }

    private IOReactorConfig getIOReactorConfig() {
        return IOReactorConfig.custom()
            .setIoThreadCount(Constant.CPU_NO)
            .setSoKeepAlive(true)
            .build();
    }

    private SSLContext getSSLContext() {
        try {
            X509TrustManager[] tm = new X509TrustManager[]{new HttpsTrustManager(fileName, password)};
            SSLContext sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            sslContext.init(null, tm, new SecureRandom());
            return sslContext;
        } catch (NoSuchAlgorithmException e) {
            throw new GwException(GwErrorEnum.HTTPS_CERTIFICATE_ERROR, "NoSuchAlgorithmException", e);
        } catch (KeyManagementException e) {
            throw new GwException(GwErrorEnum.HTTPS_CERTIFICATE_ERROR, "KeyManagementException", e);
        } catch (CertificateException e) {
            throw new GwException(GwErrorEnum.HTTPS_CERTIFICATE_ERROR, "CertificateException", e);
        } catch (KeyStoreException e) {
            throw new GwException(GwErrorEnum.HTTPS_CERTIFICATE_ERROR, "KeyStoreException", e);
        } catch (IOException e) {
            throw new GwException(GwErrorEnum.HTTPS_CERTIFICATE_ERROR, "IOException", e);
        }
    }

    private SSLIOSessionStrategy getSSLIOSessionStrategy() {
        return new SSLIOSessionStrategy(getSSLContext(), sslProtocols, null, NoopHostnameVerifier.INSTANCE);
    }

    private Registry<SchemeIOSessionStrategy> getRegistry() {
        return RegistryBuilder.<SchemeIOSessionStrategy>create()
            .register(HTTP, NoopIOSessionStrategy.INSTANCE)
            .register(HTTPS, getSSLIOSessionStrategy())
            .build();
    }

    private PoolingNHttpClientConnectionManager getConnectionManager() throws IOReactorException {
        ioReactor = new DefaultConnectingIOReactor(getIOReactorConfig());
        manager = new PoolingNHttpClientConnectionManager(ioReactor, getRegistry());
        manager.setDefaultMaxPerRoute(maxConnectionsPerHost);
        manager.setMaxTotal(maxTotalConnections);
        return manager;
    }

    private NHttpClientEventHandler getEventHandler() {
        return new HttpAsyncRequestExecutor();
    }


    /**
     * 格式化URL
     */
    public String formateUri(String url) {
        Assert.notNull(url, "URL为空");

        if (!url.startsWith(HTTPS)) {
            return HTTPS + "://" + url;
        }

        return url;
    }

    /**
     * Post连接设置
     */
    public HttpPost getPostBean(String url, String content, String certId) {
        if (log.isDebugEnabled()) {
            log.debug("HttpsAsyncClient.post send: url={}, content={}, certId={}", url, content, certId);
        }
        HttpPost post = new HttpPost(formateUri(url));
        String signature = ValidateUtils.sign(content, certId);
        //设置Header
        post.setHeader("Content-Type", "application/xml; charset=\"utf-8\"");
        post.setHeader("Signature", signature);

        // 非生产环境设置环境位和流量分组信息
        String env = ZoneClient.getInstance().getEnv().name();
        if (StringUtils.isNotBlank(env)
            && StringUtils.equalsAnyIgnoreCase(env, EnvEnum.STABLE.name(), EnvEnum.DEV.name(), EnvEnum.SIT.name())) {
            // 首先放置流量分组信息
            log.info("================================================================");
            log.info("HttpsAsyncClient转发报文时DevGroup的值{}", TraceContext.get(Constant.HTTP_FLOW_GROUP));
            log.info("HttpsAsyncClient转发报文时Environment的值{}", TraceContext.get(Constant.HTTP_FLOW_ENV));
            log.info("================================================================");

            post.setHeader(Constant.HTTP_FLOW_GROUP, TraceContext.get(Constant.HTTP_FLOW_GROUP));
            post.setHeader(Constant.HTTP_FLOW_ENV, TraceContext.get(Constant.HTTP_FLOW_ENV));
        }

        //设置Body
        post.setEntity(new StringEntity(content, Constant.CHARTSET));
        //记录发送报文日志
        LoggerUtils.logMsg(InfoCacheUtils.getPbocInf(), signature, content);
        return post;
    }

    public HttpPost getPost(String institution,String msgTp, String content) {
        return getPostBean(InfoCacheUtils.getInstUrl(institution,msgTp), content,
            InfoCacheUtils.getPbocSignCertDnOrNickname(institution));
    }

    public Future<HttpResponse> post(String institution,String msgTp, String content, FutureCallback<HttpResponse> callback) {
        return post(getPost(institution,msgTp, content), callback);
    }

    public Future<HttpResponse> post(HttpPost request, FutureCallback<HttpResponse> callback) {
        return client.execute(request, callback);
    }

    public void releaseConnection() {
        try {
            client.close();
            connEvictor.shutdown();
        } catch (IOException e) {
            log.error("HttpsCallBack.releaseConnection error: {}", e);
        }
    }

    public void logExceptionEvent() {
        for (ExceptionEvent event : ioReactor.getAuditLog()) {
            log.error("IOExceptionEvent: {}", event.toString());
        }
    }

    public static class IdleConnectionEvictor extends Thread {

        private final PoolingNHttpClientConnectionManager connMgr;
        private long sleepTime;
        private long idleTime;

        private volatile boolean shutdown;

        public IdleConnectionEvictor(PoolingNHttpClientConnectionManager connMgr, long sleepTime, long idleTime) {
            super("Async connection evictor");
            this.connMgr = connMgr;
            this.sleepTime = sleepTime;
            this.idleTime = idleTime;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(sleepTime);
                        connMgr.closeExpiredConnections();
                        connMgr.closeIdleConnections(idleTime, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException e) {
                log.error("closeExpiredConnectionsException: {}", e);
                Thread.currentThread().interrupt();
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }
}
