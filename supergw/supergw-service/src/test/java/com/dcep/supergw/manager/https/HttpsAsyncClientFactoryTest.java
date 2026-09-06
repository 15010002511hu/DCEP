package com.dcep.supergw.manager.https;

import com.dcep.supergw.Aplication;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Aplication.class)
@RunWith(JMockit.class)
public class HttpsAsyncClientFactoryTest {

    @Mocked(stubOutClassInitialization = true)
    HttpsAsyncClient httpsAsyncClient;

    /**
     * 分支 client.getClient().isRunning() == false
     */
    @Test
    public void testGetInstance_IsRunningFalse() {
        new Expectations() {
            {
                httpsAsyncClient.getClient().isRunning();
                result = false;

            }
        };
        HttpsAsyncClientFactory.getInstance();
    }

    /**
     * 分支 client.getClient().isRunning() == true
     */
    @Test
    public void testGetInstance_IsRunningTrue() {
        new Expectations() {
            {
                httpsAsyncClient.getClient().isRunning();
                result = true;

            }
        };
        HttpsAsyncClientFactory.getInstance();
    }
}
