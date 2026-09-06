package com.dcep.supergw.manager.https;

import com.dcep.infocache.manager.NacosConsume;
import com.dcep.supergw.Aplication;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import java.io.IOException;
import mockit.Capturing;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(JMockit.class)
@SpringBootTest(classes = Aplication.class)
public class HttpsClientTest {

    @Capturing
    CloseableHttpResponse closeableHttpResponse;
    @Tested
    CloseableHttpClient closeableHttpClient;
    @Mocked(stubOutClassInitialization = true)
    HttpsClientFactory httpsFactory;
    @Mocked
    ValidateUtils validateUtils;
    @Mocked
    SoapUtils soapUtils;
    @Mocked(stubOutClassInitialization = true)
    LoggerUtils loggerUtils;
    @Mocked
    EntityUtils entityUtils;
    @Mocked
    InfoCacheUtils infoCacheUtils;
    @Mocked
    NacosConsume nacosConsume;


    /**
     * 测试构造方法
     */
    @Test
    public void testConstructer() {
        HttpsClient client = new HttpsClient();
    }

    @Test
    public void test_HttpsClient_formateUri_https() {
        HttpsClient.formateUri("https://0.0.0.0");
    }

    @Test
    public void test_HttpsClient_formateUri_http() {
        HttpsClient.formateUri("http://0.0.0.0");
    }

    /**
     * response code 200 NacosConsume.getIsSign() true
     */
    @Test
    public void test_HttpsClientPost_Status200_IsSign_True() {
        new Expectations() {
            {
                closeableHttpResponse.getStatusLine().getStatusCode();
                result = 200;
            }
        };
        new Expectations(NacosConsume.class) {
            {
                NacosConsume.getIsSign();
                result = true;
            }
        };

        HttpsClient.post("https://0.0.0.0", null, "", "");
    }

    /**
     * response code 200 NacosConsume.getIsSign() true
     */
    @Test
    public void test_HttpsClientPostfinally1() {

        new Expectations() {
            {
                closeableHttpResponse.getStatusLine().getStatusCode();
                result = 200;
            }
        };
        new Expectations() {
            {
                closeableHttpResponse.getEntity();
                result = null;
            }
        };

        HttpsClient.post("https://0.0.0.0", null, "", "");
    }

    /**
     * response code 200 NacosConsume.getIsSign() false
     */
    @Test
    public void test_HttpsClientPost_Status200_IsSign_False() {
        new Expectations() {
            {
                closeableHttpResponse.getStatusLine().getStatusCode();
                result = 200;
            }
        };
        new Expectations(NacosConsume.class) {
            {
                NacosConsume.getIsSign();
                result = false;
            }
        };

        HttpsClient.post("https://0.0.0.0", null, "", "");
    }

    /**
     * resonse code 500 非200
     */
    @Test(expected = GwException.class)
    public void test_HttpsClientPost_Status500() {
        new Expectations() {
            {
                closeableHttpResponse.getStatusLine().getStatusCode();
                result = 500;
            }
        };

        HttpsClient.post("https://0.0.0.0", null, "", "");
    }

    /**
     * resonse code 500 非200
     */
    @Test(expected = GwException.class)
    public void test_HttpsClientPost_Statusfail1() {
        new Expectations() {
            {
                closeableHttpResponse.getStatusLine().getStatusCode();
                result = new ClientProtocolException();
            }
        };

        HttpsClient.post("https://0.0.0.0", null, "", "");
    }

    /**
     * resonse code 500 非200
     */
    @Test(expected = GwException.class)
    public void test_HttpsClientPost_Statusfail2() {
        new Expectations() {
            {
                closeableHttpResponse.getStatusLine().getStatusCode();
                result = new IOException();
            }
        };

        HttpsClient.post("https://0.0.0.0", null, "", "");
    }

    @Test(expected = GwException.class)
    public void test_HttpsClientPost_Statusfail3() {
        new Expectations() {
            {
                HttpsClientFactory.getHttpClient();
                result = new Error();
            }
        };

        HttpsClient.post("https://0.0.0.0", null, "", "");
    }
}
