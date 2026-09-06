package com.dcep.supergw.common.config;

import com.dcep.supergw.Aplication;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;

@RunWith(JMockit.class)
@SpringBootTest(classes = Aplication.class)
public class UndertowWithoutTraceConfigTest {

    @Test
    public void undertowWithoutTraceConfig1() {
        UndertowWithoutTraceConfig testConfig = new UndertowWithoutTraceConfig();
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        testConfig.customize(factory);
    }

}
