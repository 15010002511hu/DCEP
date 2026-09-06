package com.dcep.supergw.common.enums;

import com.dcep.supergw.Aplication;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : maxinyu
 * @version : GwErrorEnumTest.java v 0.1 2020-02-24
 * @description :
 */
@SpringBootTest(classes = Aplication.class)
@RunWith(JMockit.class)
public class GwErrorEnumTest {

    /**
     * 测试 getEnum方法 错误码存在
     */
    @Test
    public void tetGetEnumCodeExist() {
        String errorCode = "DCEPO1012";
        GwErrorEnum.getEnum(errorCode);
    }

    /**
     * 测试 getEnum方法 错误码不存在
     */
    @Test
    public void tetGetEnumCodeNotExist() {
        String errorCode = "DCEP0000000";
        GwErrorEnum.getEnum(errorCode);
    }
}
