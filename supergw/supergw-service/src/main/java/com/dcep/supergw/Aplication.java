/**
 *
 */
package com.dcep.supergw;

import com.dcep.infocache.manager.NacosConsume;
import com.dcep.supergw.common.config.DtoMappingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Administrator
 */
@SpringBootApplication
@ServletComponentScan
@EnableAsync
@Slf4j
public class Aplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = SpringApplication.run(Aplication.class, args);
        ConfigurableEnvironment env = ctx.getEnvironment();
        MutablePropertySources m = env.getPropertySources();
        for (org.springframework.core.env.PropertySource<?> p : m) {
            log.info("config--->>>>>" + p.getName() + ":" + p.getSource());
        }

        DtoMappingConfig.loadDTO();
        log.info("验签开关为：{}, 加密开关为：{}", NacosConsume.getIsSign(), NacosConsume.getIsEncrypt());
    }

}
