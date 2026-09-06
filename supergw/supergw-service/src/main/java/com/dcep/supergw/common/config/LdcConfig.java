package com.dcep.supergw.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author : maxinyu
 * @version : LdcConfig.java v 0.1 2019-09-02
 * @description : 扫描LdcBean
 */
@Configuration
@BeansCondition(name = "beans.config", havingValue = "LdcConfig")
@ComponentScan("com.dubbo.ldc")
public class LdcConfig {

}
