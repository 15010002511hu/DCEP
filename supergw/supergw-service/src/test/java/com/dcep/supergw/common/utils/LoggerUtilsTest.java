package com.dcep.supergw.common.utils;

import com.dcep.infocache.OrgCache;
import com.dcep.supergw.Aplication;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Aplication.class)
@RunWith(JMockit.class)
public class LoggerUtilsTest {

    @Tested
    LoggerUtils loggerUtils;

    @Mocked
    OrgCache orgCache;

    @Test
    public void test_loggerUtils_getInstLogger_succ() {

    }

    @Test
    public void test_loggerUtils_getInstLogger_instCodeNotValid() {

    }

    @Test
    public void test_getLogger() {
        loggerUtils.getLogger("abc");
        loggerUtils.getLogger("005000");
    }

    @Test
    public void test_loggerUtils_logMsg_succ() {
        loggerUtils.logMsg("C1010511003703", "unit test sign", "unit test xml");
    }

    @Test
    public void test_loggerUtils_logMsg_fail() {
//		new Expectations() {
//			{
//				loggerUtils.getInstLogger(anyString);
//				result = null;
//			}
//		};

        loggerUtils.logMsg("C1010511003703", "unit test sign", "unit test xml".getBytes());
    }
}
