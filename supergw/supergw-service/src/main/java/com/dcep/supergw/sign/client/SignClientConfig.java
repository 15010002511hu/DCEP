package com.dcep.supergw.sign.client;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.dcep.common.nacos.DcepListener;
import com.dcep.supergw.common.config.BeansCondition;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * 多IDC情况下，本配置生效的前提是不同的IDC配置中心配置不同的签名服务器地址！ 经与安全组、架构组确认，目前配置中心采用上述模式部署。
 *
 * @author laimincai
 * @version $Id: SignClientConfig.java, v 0.1 2021年3月4日 下午3:33:40 laimincai Exp $
 */
@Configuration
@BeansCondition(name = "beans.config", havingValue = "SignClientConfig")
public class SignClientConfig {

    private static Logger logger = LoggerFactory.getLogger(SignClientConfig.class);

    private static final String CSSCONFIG_DATAID = "kms_tcp_config_hlht.properties";
    private static final String CSSCONFIG_GROUP = "DEFAULT_GROUP";

    //Nacos配置中心服务
    @NacosInjected
    private ConfigService configService;

    @PostConstruct
    public void init() throws Exception {
        //获取签名服务器配置信息
        String metaInfo = configService.getConfig(CSSCONFIG_DATAID, CSSCONFIG_GROUP, 1000);
        logger.info("load cssconfig.properties: {}", metaInfo);
        //加载本地签名服务客户端配置信息
        SignClient.refresh(metaInfo);

        //添加配置中心监听器
        configService.addListener(CSSCONFIG_DATAID, CSSCONFIG_GROUP, new DcepListener(1000) {
            @Override
            public void onReceived(String configInfo) throws Exception {
                //刷新本地签名服务客户端配置信息
                logger.info("refresh cssconfig.properties: {}", configInfo);
                SignClient.refresh(configInfo);
            }
        });
    }
}
