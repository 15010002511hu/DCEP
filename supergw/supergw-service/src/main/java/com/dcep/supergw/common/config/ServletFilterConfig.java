/*
 * pbcdci.cn Inc. Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.common.config;

import com.dcep.supergw.controller.filter.EnvFilter;
import com.dcep.supergw.controller.filter.EnvFlagFilter;
import com.dcep.supergw.controller.filter.FlowFilter;
import com.dcep.supergw.controller.filter.PathPatternFilter;
import com.dcep.supergw.controller.filter.SoapFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * servlet过滤器配置类 注意： 全链路span设置也是采用过滤器方式实现，为保证全链路filter优先级最高，约定： 1、全链路filter采用Ordered.HIGHEST_PRECEDENCE方式配置；
 * 2、网关业务filter采用数字1开始配置。
 *
 * @author laimincai
 * @date 2023/11/30
 */
@Configuration
public class ServletFilterConfig {

    @Bean
    public FilterRegistrationBean<SoapFilter> registSoapFilter() {
        FilterRegistrationBean<SoapFilter> registrationBean = new FilterRegistrationBean<SoapFilter>();
        registrationBean.setFilter(new SoapFilter());
        registrationBean.addUrlPatterns("/dcep", "/exploring", "/infsync");
        registrationBean.setOrder(1);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<EnvFilter> registEnvFilter() {
        FilterRegistrationBean<EnvFilter> registrationBean = new FilterRegistrationBean<EnvFilter>();
        registrationBean.setFilter(new EnvFilter());
        registrationBean.addUrlPatterns("/dcep", "/exploring", "/infsync");
        registrationBean.setOrder(2);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<PathPatternFilter> registPathPatternFilter() {
        FilterRegistrationBean<PathPatternFilter> registrationBean =
                new FilterRegistrationBean<PathPatternFilter>();
        registrationBean.setFilter(new PathPatternFilter());
        registrationBean.addUrlPatterns("/dcep", "/exploring", "/infsync");
        registrationBean.addInitParameter("/dcep", "^dcep\\.(?!466|991)\\d{3}\\.\\d{3}\\.\\d{2}$");
        registrationBean.addInitParameter("/infsync", "^dcep\\.466\\.\\d{3}\\.\\d{2}$");
        registrationBean.addInitParameter("/exploring", "^dcep\\.991\\.\\d{3}\\.\\d{2}$");
        registrationBean.setOrder(5);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<EnvFlagFilter> registEnvFlagFilter() {
        FilterRegistrationBean<EnvFlagFilter> registrationBean = new FilterRegistrationBean<EnvFlagFilter>();
        registrationBean.setFilter(new EnvFlagFilter());
        registrationBean.addUrlPatterns("/dcep", "/exploring", "/infsync");
        registrationBean.setOrder(20);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<FlowFilter> registFlowFilter() {
        FilterRegistrationBean<FlowFilter> registrationBean = new FilterRegistrationBean<FlowFilter>();

        registrationBean.setFilter(new FlowFilter());
        registrationBean.addUrlPatterns("/dcep", "/exploring", "/infsync");
        registrationBean.setOrder(30);
        return registrationBean;
    }
}
