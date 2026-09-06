package com.dcep.supergw.manager.https;

import com.dcep.supergw.Aplication;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : maxinyu
 * @version :  v 0.1 2020-02-28
 * @description :
 */
@RunWith(JMockit.class)
@SpringBootTest(classes = Aplication.class)
public class HttpsClientFactoryTest {

    private static HttpsClientFactory httpsClientFactory;

    /**
     * 测试构造方法
     */
    @Test
    public void testConstructer() {

        HttpsClientFactory httpsClientFactory = new HttpsClientFactory();
    }

    /**
     * 测试 getHttpClient
     */
    @Test
    public void testGetHttpClient() {

        HttpsClientFactory.getHttpClient();
    }

    @Test
    public void testPrivate1()
        throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class c = HttpsClientFactory.class;
        httpsClientFactory = new HttpsClientFactory();
        Method method = c.getDeclaredMethod("getSSLContext", null);
        method.setAccessible(true);

        method.invoke(httpsClientFactory, null);
    }

    @Test(expected = Exception.class)
    public void testPrivate2()
        throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class c = HttpsClientFactory.class;
        httpsClientFactory = new HttpsClientFactory();
        Method method = c.getDeclaredMethod("getSSLContext", null);
        method.setAccessible(true);
        new MockUp<SSLContext>(SSLContext.class) {
            @Mock
            SSLContext getInstance(String service) throws NoSuchAlgorithmException {
                throw new NoSuchAlgorithmException();
            }
        };
        method.invoke(httpsClientFactory, null);
    }

    @Test(expected = Exception.class)
    public void testPrivate3()
        throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class c = HttpsClientFactory.class;
        httpsClientFactory = new HttpsClientFactory();
        Method method = c.getDeclaredMethod("getSSLContext", null);
        method.setAccessible(true);
        new MockUp<SSLContext>(SSLContext.class) {
            @Mock
            SSLContext getInstance(String service) throws KeyManagementException {
                throw new KeyManagementException();
            }
        };
        method.invoke(httpsClientFactory, null);
    }

    @Test(expected = Exception.class)
    public void testPrivate4()
        throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class c = HttpsClientFactory.class;
        httpsClientFactory = new HttpsClientFactory();
        Method method = c.getDeclaredMethod("getSSLContext", null);
        method.setAccessible(true);
        new MockUp<SSLContext>(SSLContext.class) {
            @Mock
            SSLContext getInstance(String service) throws CertificateException {
                throw new CertificateException();
            }
        };
        method.invoke(httpsClientFactory, null);
    }

    @Test(expected = Exception.class)
    public void testPrivate5()
        throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class c = HttpsClientFactory.class;
        httpsClientFactory = new HttpsClientFactory();
        Method method = c.getDeclaredMethod("getSSLContext", null);
        method.setAccessible(true);
        new MockUp<SSLContext>(SSLContext.class) {
            @Mock
            SSLContext getInstance(String service) throws KeyStoreException {
                throw new KeyStoreException();
            }
        };
        method.invoke(httpsClientFactory, null);
    }

    @Test(expected = Exception.class)
    public void testPrivate6()
        throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class c = HttpsClientFactory.class;
        httpsClientFactory = new HttpsClientFactory();
        Method method = c.getDeclaredMethod("getSSLContext", null);
        method.setAccessible(true);
        new MockUp<SSLContext>(SSLContext.class) {
            @Mock
            SSLContext getInstance(String service) throws IOException {
                throw new IOException();
            }
        };
        method.invoke(httpsClientFactory, null);
    }

    @Test
    public void testPrivate7()
        throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<HttpsClientFactory> c = HttpsClientFactory.class;
        httpsClientFactory = new HttpsClientFactory();
        Method method = c.getDeclaredMethod("getSSLContext", null);
        method.setAccessible(true);

        method.invoke(httpsClientFactory, null);
    }


    @Test
    public void testPrivate8()
        throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<HttpsClientFactory> c = HttpsClientFactory.class;
        httpsClientFactory = new HttpsClientFactory();
        Method method = c.getDeclaredMethod("getConnectionManager", null);
        method.setAccessible(true);

        method.invoke(httpsClientFactory, null);
    }

}
