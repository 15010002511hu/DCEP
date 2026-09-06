/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author huyajun
 * @version $Id: DubboConfig.java, v 0.1 2019年6月18日 上午10:53:23 Administrator Exp $
 */

@Configuration
@BeansCondition(name = "beans.config", havingValue = "DubboConfig")
@ImportResource(locations = {"classpath*:META-INF/spring/*-api.xml"})
public class DubboConfig {

}
