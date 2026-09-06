package com.dcep.supergw.common.exception;

import com.dcep.common.exception.DcepException;
import com.dcep.supergw.Aplication;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : maxinyu
 * @version : GwExceptionTest.java v 0.1 2020-02-28
 * @description :
 */
@RunWith(JMockit.class)
@SpringBootTest(classes = Aplication.class)
public class GwExceptionTest {

    /**
     * 测试构造方法  GwException(DcepException e)
     */
    @Test
    public void testConstructer_Param_DcepException() {
        GwException gwException = new GwException(new DcepException());
    }

    /**
     * 测试构造方法 GwException(String message, Throwable cause)
     */
    @Test
    public void testConstructer_Param_StringThrowable() {
        Throwable throwable = new Throwable();
        GwException gwException = new GwException("test", throwable);
    }
}
