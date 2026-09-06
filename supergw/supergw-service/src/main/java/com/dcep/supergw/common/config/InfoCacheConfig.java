package com.dcep.supergw.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 引入信息缓存
 *
 * @author linlu
 * @date 20190717
 */
@Configuration
@BeansCondition(name = "beans.config", havingValue = "InfoCacheConfig")
@ComponentScan(basePackages = "com.dcep.infocache.*")
public class InfoCacheConfig {

}
