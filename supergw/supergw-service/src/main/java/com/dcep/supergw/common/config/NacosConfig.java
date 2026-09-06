package com.dcep.supergw.common.config;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.dcep.common.nacos.DcepListener;
import com.dcep.supergw.sign.client.SignClientConfig;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author maxinyu
 * @date 2023/6/19 17:21
 */
@Configuration
public class NacosConfig {

    private static Logger logger = LoggerFactory.getLogger(SignClientConfig.class);

    @Value("${supergw.nacos.dataId}")
    private String configDataId;
    @Value("${supergw.nacos.groupId}")
    private String configGroup;

    //Nacos配置中心服务
    @NacosInjected
    private ConfigService configService;

    @PostConstruct
    public void init() throws Exception {
        //获取配置信息
        String metaInfo = configService.getConfig(configDataId, configGroup, 1000);
        logger.info("load supergw.nacos.properties: {}", metaInfo);
        //加载客户端配置信息
        NacosConfigClient.getInstance().refresh(metaInfo);

        //添加配置中心监听器
        configService.addListener(configDataId, configGroup, new DcepListener(1000) {
            @Override
            public void onReceived(String configInfo) throws Exception {
                //刷新客户端配置信息
                logger.info("refresh supergw.nacos.properties: {}", configInfo);
                NacosConfigClient.getInstance().refresh(configInfo);
            }
        });
    }
}
