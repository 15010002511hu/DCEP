/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.controller.filter;

import com.dcep.supergw.common.constant.Constant;
import com.dcepex.trace.support.TraceContext;
import com.dubbo.ldc.ZoneClient;
import com.dubbo.ldc.constant.EnvEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 一体化测试用，获取request的header标并设置到TraceContext
 *
 * @author qinchaoyong
 * @date 2023/10/31 14:40
 */
@Slf4j
public class EnvFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            // 一体化测试添加，用于设置流量分组信息
            setHttpFlowGroup((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        } catch (Exception e) {
            log.error("设置流量分组及环境信息时报错:{}", e.getMessage(), e);
        } finally {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 设置流量分组信息和环境信息
     *
     * @param req  http请求
     * @param resp http响应
     */
    private void setHttpFlowGroup(HttpServletRequest req, HttpServletResponse resp) {
        String env = req.getHeader(Constant.HTTP_FLOW_ENV);
        if (StringUtils.isNotBlank(env) && StringUtils.equalsAnyIgnoreCase(env, EnvEnum.STABLE.name(), EnvEnum.DEV.name(), EnvEnum.SIT.name())) {
            // 与本地当前实际环境进行匹配判断，如果不是stable和dev及sit，就打印告警并且不设置环境和分组信息
            String localEnv = ZoneClient.getInstance().getEnv().name();
            if (!StringUtils.equalsAnyIgnoreCase(localEnv, EnvEnum.STABLE.name(), EnvEnum.DEV.name(), EnvEnum.SIT.name())) {
                log.warn("机构将{}的报文发送到{}了", env, localEnv);
                return;
            }

            // 首先放置流量分组信息
            TraceContext.set(Constant.HTTP_FLOW_GROUP, req.getHeader(Constant.HTTP_FLOW_GROUP));
            TraceContext.set(Constant.HTTP_FLOW_ENV, env);

            // 响应头带出流量分组信息和环境信息
            resp.setHeader(Constant.HTTP_FLOW_GROUP, TraceContext.get(Constant.HTTP_FLOW_GROUP));
            resp.setHeader(Constant.HTTP_FLOW_ENV, TraceContext.get(Constant.HTTP_FLOW_ENV));
        }
    }
}
