package com.dcep.supergw.common.config;

import com.alibaba.nacos.api.config.ConfigService;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author maxinyu
 * @date 2023/6/20 19:45
 */
@RunWith(JMockit.class)
public class NacosConfigTest {

    @Tested
    NacosConfig nacosConfig;

    @Injectable
    ConfigService configService;

    @Mocked
    NacosConfigClient nacosConfigClient;

    @Test
    public void test() {
        try {
            nacosConfig.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
