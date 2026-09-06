package com.dcep.supergw.common.utils;

import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author : maxinyu
 * @version : SequenceGenTest.java v 0.1 2019-11-12
 * @description :
 */
@RunWith(JMockit.class)
public class SequenceGenTest {

    /**
     * 测试无参方法，返回一个序列 yyyyMMddHHmmssS+ 00000000~99999999
     */
    @Test
    public void testGenSequenceId() {
        new MockUp<String>(String.class) {
            @Mock
            public int compareTo(String anotherString) {
                return 1;
            }
        };
        String seqId = SequenceGen.genSequenceId();
    }

    /**
     * 测试分支 currentDateStr.compareTo(lastDateStr)《=0
     */
    @Test
    public void testGenSequenceId_CompareToLessThen0() {
        new MockUp<String>(String.class) {
            @Mock
            public int compareTo(String anotherString) {
                return 0;
            }
        };
        String seqId = SequenceGen.genSequenceId();
    }

    /**
     * 测试静态代码块，StringUtils.isEmpty false;
     */
    @Test
    public void testStatic_StringUtilsIsEmpty_false() {
        new Expectations(StringUtils.class) {
            {
                StringUtils.isEmpty(anyString);
                result = false;
            }
        };
        new SequenceGen();
    }

    @Test
    public void testStatic_StringUtilsIsEmpty() {

        new SequenceGen();
    }

}
