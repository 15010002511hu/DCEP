package com.dcep.supergw.common.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : duzhong
 * @version : SentinelConfig.java v 0.1 2020-05-15
 * @description : Sen从nacos获取配置
 */

@Configuration
@BeansCondition(name = "beans.config", havingValue = "SentinelConfig")
public class SentinelConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value(value = "${nacos.config.server-addr}")
    String address;

    @Value(value = "${nacos.config.username}")
    String username;

    @Value(value = "${nacos.config.password}")
    String password;

    @Value(value = "${nacos.config.namespace}")
    String namespace;

    @Value(value = "${sentinel.groupId}")
    String groupId;

    @Value(value = "${sentinel.dataId.flow}")
    String flowDataId;

    @Value(value = "${sentinel.dataId.degrade}")
    String degradeDataId;

    @PostConstruct
    public void loadFlowRules() {

        Gson gson = new Gson();

        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, address);
        properties.setProperty(PropertyKeyConst.USERNAME, username);
        properties.setProperty(PropertyKeyConst.PASSWORD, password);
        properties.setProperty(PropertyKeyConst.NAMESPACE, namespace);

        ReadableDataSource<String, List<FlowRule>> flowRules = new NacosDataSource<List<FlowRule>>(properties, groupId,
            flowDataId, json -> gson.fromJson(json, new TypeToken<List<FlowRule>>() {
            private static final long serialVersionUID = 9173566459355512221L;
        }.getType()));
        FlowRuleManager.register2Property(flowRules.getProperty());

        log.info("限流参数:{}", FlowRuleManager.getRules());

        ReadableDataSource<String, List<DegradeRule>> degradeRules = new NacosDataSource<List<DegradeRule>>(properties,
            groupId,
            degradeDataId, json -> gson.fromJson(json, new TypeToken<List<DegradeRule>>() {
            private static final long serialVersionUID = 9173566459355512221L;
        }.getType()));
        DegradeRuleManager.register2Property(degradeRules.getProperty());

        log.info("熔断参数:{}", DegradeRuleManager.getRules());

    }

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

}
