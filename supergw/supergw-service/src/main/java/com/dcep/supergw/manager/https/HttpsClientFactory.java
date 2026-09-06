package com.dcep.supergw.manager.https;

import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.EnviromentUtils;
import com.dcep.supergw.manager.https.interceptor.EnvFlagClientInterceptor;
import com.dcepex.trace.support.http.HttpclientInterceptor;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.IdleConnectionEvictor;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.logging.log4j.core.util.Integers;


public class HttpsClientFactory {

    private final static String HTTP = "http";
    private final static String HTTPS = "https";
    /* 0730改动-根据阿里巴巴代码扫描规范修改静态常量名为大写字母 */
    private final static String FILENAME = EnviromentUtils.get("https.cert.keystore");
    private final static String PASSWORD = EnviromentUtils.get("https.cert.password");
    private final static int MAX_CONNECTIONS_PERHOST = Integers
        .parseInt(EnviromentUtils.get("https.pool.maxConnectionsPerHost"));
    private final static int MAX_TOTAL_CONNECTIONS = Integers
        .parseInt(EnviromentUtils.get("https.pool.maxTotalConnections"));
    private final static int CONNECTION_REQUEST_TIMEOUT = Integers
        .parseInt(EnviromentUtils.get("https.timeout.connectionRequest"));
    private final static int CONNECTION_TIMEOUT = Integers.parseInt(EnviromentUtils.get("https.timeout.connection"));
    private final static int SOCKET_TIMEOUT = Integers.parseInt(EnviromentUtils.get("https.timeout.socket"));
    private final static String[] SSL_PROTOCOLS = EnviromentUtils.get("https.protocol.ssl", String[].class, null);
    private final static long SLEEP_TIME = EnviromentUtils.get("https.evictor.sleepTime", Long.class, 5L);
    private final static long MAX_IDLE_TIME = EnviromentUtils.get("https.evictor.maxIdleTime", Long.class, 5L);

    private volatile static CloseableHttpClient httpClient;
    private static PoolingHttpClientConnectionManager manager;
    private static IdleConnectionEvictor connEvictor;

    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (CloseableHttpClient.class) {
                if (httpClient == null) {
                    httpClient = HttpClientBuilder.create().disableAutomaticRetries() // 关闭重试
                        .setConnectionManager(getConnectionManager()).setDefaultRequestConfig(getRequestConfig())
                        .setRequestExecutor(getRequestExcecutor())
                        // 配合全链路监控修改，增加请求相应的拦截器
                        .addInterceptorLast((HttpRequestInterceptor) new HttpclientInterceptor())
                        .addInterceptorFirst((HttpResponseInterceptor) new HttpclientInterceptor())
                        // 增加环境位相关的拦截器
                        .addInterceptorFirst((HttpRequestInterceptor) new EnvFlagClientInterceptor())
                        .addInterceptorLast((HttpResponseInterceptor) new EnvFlagClientInterceptor())
                        .build();

                    connEvictor = new IdleConnectionEvictor(manager,
                        SLEEP_TIME, TimeUnit.SECONDS,
                        MAX_IDLE_TIME, TimeUnit.SECONDS
                    );
                    connEvictor.start();
                }
            }
        }
        return httpClient;
    }

    private static SSLContext getSSLContext() {
        try {
            X509TrustManager[] tm = new X509TrustManager[]{new HttpsTrustManager(FILENAME, PASSWORD)};
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

    private static SSLConnectionSocketFactory getSocketFactory() {
        return new SSLConnectionSocketFactory(getSSLContext(), SSL_PROTOCOLS, null, NoopHostnameVerifier.INSTANCE);
    }

    private static RequestConfig getRequestConfig() {
        return RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
            .setConnectTimeout(CONNECTION_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT)
            .setCookieSpec(CookieSpecs.IGNORE_COOKIES).setExpectContinueEnabled(Boolean.FALSE)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
    }

    private static HttpRequestExecutor getRequestExcecutor() {
        return new HttpRequestExecutor();
    }

    private static Registry<ConnectionSocketFactory> getRegistry() {
        return RegistryBuilder.<ConnectionSocketFactory>create().register(HTTP, PlainConnectionSocketFactory.INSTANCE)
            .register(HTTPS, getSocketFactory()).build();
    }

    private static PoolingHttpClientConnectionManager getConnectionManager() {
        manager = new PoolingHttpClientConnectionManager(getRegistry());
        manager.setDefaultMaxPerRoute(MAX_CONNECTIONS_PERHOST);
        manager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        return manager;
    }

}
