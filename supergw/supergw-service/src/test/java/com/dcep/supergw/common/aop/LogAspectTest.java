package com.dcep.supergw.common.aop;

import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(JMockit.class)
@SpringBootTest
public class LogAspectTest {

    @Tested
    LogAspect logAspect;

    @Test

    public void aroundTest(@Mocked ProceedingJoinPoint pj) {
        new MockUp<ProceedingJoinPoint>(ProceedingJoinPoint.class) {
            @Mock
            public void proceed() {

            }
        };
        new Expectations() {
            {
                pj.getArgs();
                result = new String[]{"1", "2"};

            }
        };

        try {
            logAspect.around(pj);
        } catch (Throwable e) {
//			e.printStackTrace();
        }

    }

}
