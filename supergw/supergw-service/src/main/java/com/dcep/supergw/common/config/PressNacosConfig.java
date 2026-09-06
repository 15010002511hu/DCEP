package com.dcep.supergw.common.config;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.dcep.common.nacos.DcepListener;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PressNacosConfig {

    private static Logger logger = LoggerFactory.getLogger(PressNacosConfig.class);

    @Value("${supergw.pressNacos.dataId}")
    String dataId;
    @Value("${supergw.pressNacos.groupId}")
    String groupId;

    //Nacos配置中心服务
    @NacosInjected
    private ConfigService configService;

    @PostConstruct
    public void init() throws Exception {
        //获取配置信息
        String metaInfo = configService.getConfig(dataId, groupId, 1000);
        logger.info("load press properties: {}", metaInfo);
        //加载客户端配置信息
        NacosConfigClient.getInstance().refreshPress(metaInfo);

        //添加配置中心监听器
        configService.addListener(dataId, groupId, new DcepListener(1000) {
            @Override
            public void onReceived(String configInfo) throws Exception {
                //刷新客户端配置信息
                logger.info("refresh press flag: {}", configInfo);
                NacosConfigClient.getInstance().refreshPress(configInfo);
            }
        });
    }
}
