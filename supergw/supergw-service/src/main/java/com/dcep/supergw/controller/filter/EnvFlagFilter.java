/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.controller.filter;

import com.dcep.common.Constants.CommonConstant;
import com.dcepex.trace.support.TraceContext;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 设置机构入向HTTP请求环境标识位拦截器
 *
 * @author laimincai
 * @date 2023/10/25
 */
public class EnvFlagFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        // 获取HTTP HEADER压测流量标识
        String loadTraffic = ((HttpServletRequest) request).getHeader(CommonConstant.LOAD_TRAFFIC_KEY);

        TraceContext.set(CommonConstant.LOAD_TRAFFIC_KEY, loadTraffic);

        chain.doFilter(request, response);
    }
}
