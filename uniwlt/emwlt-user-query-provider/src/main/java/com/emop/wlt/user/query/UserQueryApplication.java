package com.emop.wlt.user.query;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.emop.gateway.dto.common.scan.GwDtoScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author hanyabo
 * @version V1.0.0
 * @date 2024/3/29 16:00
 */
@GwDtoScan("com.emop.wlt.model.emap")
@NacosPropertySource(dataId = "uniwlt.app.properties", autoRefreshed = true)
@SpringBootApplication(scanBasePackages = {"com.dubbo.dds", "com.dubbo.ldc", "com.emop.infocache",
    "com.emop.wlt.user.query"}, exclude = {DataSourceAutoConfiguration.class})
public class UserQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserQueryApplication.class, args);
    }

}
