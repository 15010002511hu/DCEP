package com.dcep.supergw.common.config;

import com.dcep.supergw.Aplication;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(JMockit.class)
@SpringBootTest(classes = Aplication.class)
public class SentinelConfigTest {

    @Test
    public void sentinelConfigTest1() {
        SentinelConfig testConfig = new SentinelConfig();
    }

}
