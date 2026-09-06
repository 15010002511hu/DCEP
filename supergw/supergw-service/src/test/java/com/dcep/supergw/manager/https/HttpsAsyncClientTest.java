package com.dcep.supergw.manager.https;

import com.dcep.supergw.Aplication;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.EnviromentUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.ExceptionEvent;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(JMockit.class)
@SpringBootTest(classes = Aplication.class)
public class HttpsAsyncClientTest {

    private static HttpsAsyncClient client;

    @Mocked
    EnviromentUtils enviromentUtils;
    @Mocked
    InfoCacheUtils infoCacheUtils;
    @Mocked
    FutureCallback<HttpResponse> futureCallback;
    @Mocked(stubOutClassInitialization = true)
    LoggerUtils loggerUtils;
    @Mocked
    CloseableHttpAsyncClient closeableHttpAsyncClient;
    @Mocked
    Future<HttpResponse> responseFuture;
    @Mocked
    ValidateUtils validateUtils;

    @BeforeClass
    public static void init() {
        new Expectations(ValidateUtils.class) {
            {
                ValidateUtils.sign(anyString, anyString);
                result = "abc";
            }
        };

        new Expectations(EnviromentUtils.class) {
            {
                EnviromentUtils.get(anyString);
                result = "100";
                EnviromentUtils.get("https.protocol.ssl", String[].class, null);
                result = new String[]{"TLSv1.2"};
                EnviromentUtils.get(anyString, Long.class, 5L);
                result = 5L;
            }
        };
        client = new HttpsAsyncClient();
    }

    /**
     * 测试 formateUri 分支 http
     */
    @Test
    public void testFormateUri1() {
        String httpUrl = "http://127.0.0.1";
        client.formateUri(httpUrl);
    }

    /**
     * 测试 formateUri 分支 https
     */
    @Test
    public void testFormateUri2() {
        String https = "https://127.0.0.1";
        client.formateUri(https);
    }

    /**
     * 测试getPost(String url, String content, String certId)
     */


    /**
     * 测试getSSLContext 异常信息NoSuchAlgorithmException
     */
    @Test(expected = GwException.class)
    public void testSSLContext_NoSuchAlgorithmException() {
        new MockUp<SSLContext>(SSLContext.class) {
            @Mock
            public SSLContext getInstance(String var0) throws NoSuchAlgorithmException {
                throw new NoSuchAlgorithmException();
            }
        };

        HttpsAsyncClient client = new HttpsAsyncClient();
    }

    /**
     * 测试getSSLContext 异常信息KeyManagementException
     */
    @Test(expected = GwException.class)
    public void testSSLContext_KeyManagementException() {
        new MockUp<SSLContext>(SSLContext.class) {
            @Mock
            public final void init(KeyManager[] var1, TrustManager[] var2, SecureRandom var3)
                throws KeyManagementException {
                throw new KeyManagementException();
            }
        };

        HttpsAsyncClient client = new HttpsAsyncClient();
    }

    /**
     * 测试getSSLContext CertificateException
     */
    @Test(expected = GwException.class)
    public void testSSLContext_CertificateException() {
        new MockUp<HttpsTrustManager>(HttpsTrustManager.class) {
            @Mock
            public void $init(String certFile, String passwrod)
                throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
                throw new CertificateException();
            }
        };

        HttpsAsyncClient client = new HttpsAsyncClient();
    }

    /**
     * 测试getSSLContext KeyStoreException
     */
    @Test(expected = GwException.class)
    public void testSSLContext_KeyStoreException() {
        new MockUp<HttpsTrustManager>(HttpsTrustManager.class) {
            @Mock
            public void $init(String certFile, String passwrod)
                throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
                throw new KeyStoreException();
            }
        };

        HttpsAsyncClient client = new HttpsAsyncClient();
    }

    /**
     * 测试getSSLContext KeyStoreException
     */
    @Test(expected = GwException.class)
    public void testSSLContext_IOException() {
        new MockUp<HttpsTrustManager>(HttpsTrustManager.class) {
            @Mock
            public void $init(String certFile, String passwrod)
                throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
                throw new IOException();
            }
        };

        HttpsAsyncClient client = new HttpsAsyncClient();
    }

    /**
     * 测试 post方法
     */
    @Test
    public void testPost() {
        new Expectations(InfoCacheUtils.class) {
            {
                InfoCacheUtils.getInstUrl(anyString,anyString);
                result = "http://127.0.0.1";
                InfoCacheUtils.getPbocSignCertDnOrNickname(anyString);
                result = "abc";
                InfoCacheUtils.getPbocInf();
                result = "abc";
            }
        };
        new Expectations() {
            {
                closeableHttpAsyncClient
                    .execute(withInstanceOf(HttpUriRequest.class), withInstanceOf(FutureCallback.class));
                result = responseFuture;
            }
        };
        client.post("111", "111", "", futureCallback);
    }

    /**
     * 测试 releaseConnection
     */
    @Test
    public void testReleaseConnection() throws IOException {
        client.releaseConnection();
    }

    /**
     * logExceptionEvent
     */
    @Test
    public void logExceptionEvent(@Mocked DefaultConnectingIOReactor ioReactor) {
        new Expectations() {
            {
                ioReactor.getAuditLog();
                result = new Delegate() {
                    List<ExceptionEvent> getAuditLog(Invocation inv) {
                        List<ExceptionEvent> list = new ArrayList<>();
                        list.add(new ExceptionEvent(new GwException("test")));
                        return list;
                    }
                };
            }
        };
        client.logExceptionEvent();
    }


    /**
     * 测试 releaseConnection
     */
    @Test
    public void testGetHttp() throws IOException {
        client.getHTTP();
        client.getHTTPS();
        client.getClient();
        client.getFileName();
        client.getPassword();
        client.getMaxConnectionsPerHost();
        client.getMaxTotalConnections();
        client.getSocketTimeout();
        client.getConnEvictor();
        client.getSslProtocols();
        client.getSleepTime();
        client.getMaxIdleTime();
        client.getIoReactor();
        client.getManager();
        client.getClient();
        client.getConnectionRequestTimeout();
        client.getConnectionTimeout();

    }
}
