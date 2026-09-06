package com.dcep.dips.wholesalepayment.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.dcep.dips.wholesalepayment.common.utils.NetWorkUtil;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * 日志动态刷新配置
 * @author: chenxighnfeng
 */
@Slf4j
//@Service
public class LogLevelConfig {

    // 日志级别配置
    //@NacosConfigListener注解在应用启动时不会初始化，在配置信息有变化（MD5值发生变化）时才会触发事件
    @SuppressWarnings({ "unchecked" })
    @NacosConfigListener(dataId = "com.dcep.wholesale.logconfig", groupId = "DEFAULT_GROUP")
    public void refreshLogLevel(String configInfo) {
        log.info("receive nacos data: " + configInfo);
        try {
            // 1.获取nacos配置信息
            Map<String, Object> configMap = JSONObject.parseObject(configInfo, Map.class);
            if ("all".equalsIgnoreCase(configMap.get("targetIP").toString())
                || configMap.get("targetIP").toString().contains(NetWorkUtil.getHostAddress())) {
                // 2.根据配置的IP信息调整日志级别
                Map<String, Object> loggerMap = JSONObject.parseObject(configMap.get("logLevel").toString(), Map.class);
                for (Map.Entry<String, Object> entry : loggerMap.entrySet()) {
                    // 3.遍历更新不同logger日志级别
                    changeLogLevel(entry.getKey().toString(), entry.getValue().toString());
                }
            }
        } catch (Exception e) {
            log.info("--refreshLogLevel--error-->{}", e);
        }
    }

    public void changeLogLevel(String logger, String newLevel) {
        // 1.获取LoggerContext对象
        LoggerContext loggerContext = LoggerContext.getContext(false);
        // 2.获取对应logger日志配置信息
        LoggerConfig loggerConfig = loggerContext.getConfiguration().getLoggerConfig(logger);
        log.info("logger[{}] before refreshLogLevel: {}", logger, loggerConfig.getLevel());
        log.info("logger[{}] expect refreshLogLevel: {}", logger, newLevel);
        Level level = Level.toLevel(newLevel);
        // 3.更新日志级别配置
        loggerConfig.setLevel(level);
        loggerContext.updateLoggers();
        log.info("logger[{}] after refreshLogLevel: {}", logger, loggerConfig.getLevel());
    }

}
