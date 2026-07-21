package com.emop.wlt.user.management;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.emop.gateway.dto.common.scan.GwDtoScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author hanyabo
 * @version V1.0.0
 * @date 2024/3/29 16:00
 */
@EnableAsync
@GwDtoScan("com.emop.wlt.model.emap")
@NacosPropertySource(dataId = "uniwlt.app.properties", autoRefreshed = true)
@SpringBootApplication(scanBasePackages = {"com.dubbo.dds", "com.dubbo.ldc", "com.emop.infocache","com.emop.wlt.user.management"})
public class UserManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementApplication.class, args);
    }

}
