package com.dcep.supergw.common.config;

import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author maxinyu
 * @date 2023/6/20 19:51
 */
@RunWith(JMockit.class)
public class NacosConfigClientTest {


    @Test
    public void test() {
        NacosConfigClient nacosConfigClient = NacosConfigClient.getInstance();

        String str = "gray.scale.total=100\n" +
            "gray.scale.grayRate=1\n" +
            "action.config.envFlag=1";
        nacosConfigClient.refresh(str);
        nacosConfigClient.setGrayRate(100);
        nacosConfigClient.setTotal(100);
        nacosConfigClient.getGrayRate();
        nacosConfigClient.getTotal();
        nacosConfigClient.setEnvFlag("2");
        nacosConfigClient.getEnvFlag();
    }
}
