package com.dcep.supergw.common.utils;


import com.dcep.supergw.Aplication;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

/**
 * @author nieyanping
 * @version EncryptionToolUtilsTest.java, v 0.1, 2020/1/10 9:21
 * @description TODO
 */
@SpringBootTest(classes = Aplication.class)
@RunWith(JMockit.class)
public class EnvironmentTest {

    @Tested
    EnviromentUtils e;
    @Mocked
    Environment environment;

    @Test(expected = Exception.class)
    public void test_getEncryptCertId_failed1() {
        e.get("");

    }

    @Test(expected = Exception.class)
    public void test_getEncryptCertId_failed2() {
        e.get("", null, "");
    }


}