package com.dcep.supergw.common.config;

import com.dcep.supergw.Aplication;
import com.dcep.supergw.common.utils.EnviromentUtils;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : maxinyu
 * @version : dtoMappingConfigTest.java v 0.1 2019-12-19
 * @description : dtoMappingConfig测试类
 */
@RunWith(JMockit.class)
@SpringBootTest(classes = Aplication.class)
public class DtoMappingConfigTest {

    @Tested
    DtoMappingConfig dtoMappingConfig;

    @Mocked
    EnviromentUtils enviromentUtils;

    /**
     * 测试loadDTO方法
     */
    @Test
    public void testLoadDTO() {
        dtoMappingConfig.loadDTO();
    }

    /**
     * 测试getClzByMsgTp 送入存在的报文类型
     */
    @Test
    public void testGetClzByMsgTp_True() {
        dtoMappingConfig.getClzByMsgTp("dcep.201.001.01");
    }

    /**
     * 测试getClzByMsgTp 送入不存在的报文类型
     */
    @Test
    public void testGetClzByMsgTp_false() {
        dtoMappingConfig.getClzByMsgTp("000.201.001.01");
    }

    /**
     * 测试getClzByMsgTp 测试传入的参数为""
     */
    @Test
    public void testGetClzByMsgTp_blank() {
        dtoMappingConfig.getClzByMsgTp("");
    }

    /**
     * 测试getClzNameByMsgTp 送入存在的报文类型
     */
    @Test
    public void testGetClzNameByMsgTp_True() {
        dtoMappingConfig.getClzNameByMsgTp("dcep.201.001.01");
    }

    /**
     * 测试getClzNameByMsgTp 送入不存在的报文类型
     */
    @Test
    public void testGetClzNameByMsgTp_false() {
        dtoMappingConfig.getClzNameByMsgTp("000.201.001.01");
    }

}
