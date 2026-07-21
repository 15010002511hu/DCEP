package com.emop.wlt.user.query.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.emop.wlt.common.util.TemplateUtils;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author bobo
 * @Description: nacos配置监听器
 * @date 2021/4/21 下午3:15
 */
@Configuration
@Slf4j
public class NacosListeners {

    private static final String TEMPLATE_DATAID = "uniwlt.message.push.template.json";
    private static final String DEAFULT_GROUP = "DEFAULT_GROUP";
    private static final long TIMEOUTMS = 1000L;

    @NacosInjected
    private ConfigService configService;

    @PostConstruct
    public void init() {
        try {
            String templateStr = configService.getConfigAndSignListener(TEMPLATE_DATAID, DEAFULT_GROUP, TIMEOUTMS,
                new AbstractListener() {
                    @Override
                    public void receiveConfigInfo(String context) {
                        log.info("refresh nacos push template config:{}", context);
                        configToTemplateMap(context);
                    }
                });
            configToTemplateMap(templateStr);
            log.info("load nacos push template config:{}", templateStr);
        } catch (NacosException e) {
            log.error("load nacos config error!", e);
        }
    }

    /**
     * 将nacos配置加载到模板工具类中
     *
     * @param config
     */
    private void configToTemplateMap(String config) {
        List<TemplateUtils.MpsTemplate> templateList = JSON.parseArray(config, TemplateUtils.MpsTemplate.class);
        List<String> codeList = templateList.stream().map(TemplateUtils.MpsTemplate::getTemplateCode)
            .collect(Collectors.toList());
        Set<String> templateCodeKeys = TemplateUtils.TEMPLATE_MAP.keySet();
        if (templateCodeKeys.size() > 0) {
            templateCodeKeys.forEach(k -> {
                if (!codeList.contains(k)) {
                    TemplateUtils.TEMPLATE_MAP.remove(k);
                }
            });
        }
        templateList.forEach(mpsTemplate -> {
            Set<String> params = TemplateUtils.getParamsByTitleAndContent(mpsTemplate.getTitle(),
                mpsTemplate.getContent());
            mpsTemplate.setParams(params);
            TemplateUtils.TEMPLATE_MAP.put(mpsTemplate.getTemplateCode(), mpsTemplate);
        });
    }

}
