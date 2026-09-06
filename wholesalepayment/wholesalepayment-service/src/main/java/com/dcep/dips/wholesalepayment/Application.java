/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableTransactionManagement(order = 10000)
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"com.dubbo.dds", "com.dubbo.ldc"})
@ComponentScan(basePackages = {"com.dcep.infocache.*", "com.dcep.dips.wholesalepayment.*"})
@MapperScan("com.dcep.dips.wholesalepayment.dal.mapper")
@NacosPropertySource(dataId = "com.dcep.dips.wholesalepayment.properties", autoRefreshed = true)
@EnableDubbo
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
