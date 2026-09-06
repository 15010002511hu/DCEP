/*
 * pbcdci.cn Inc. Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.controller.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;

/**
 * 报文类型与URL地址映射关系校验过滤器
 * 
 * @author laimincai
 * @date 2024/01/31
 */
public class PathPatternFilter implements Filter {

    private final Map<String, Pattern> pathPatternMap = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Enumeration<String> pathPattern = filterConfig.getInitParameterNames();
        while (pathPattern.hasMoreElements()) {
            String path = pathPattern.nextElement();
            pathPatternMap.put(path, Pattern.compile(filterConfig.getInitParameter(path)));
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        SoapHeader header = (SoapHeader) request.getAttribute("header");

        Pattern pattern = pathPatternMap.get(((HttpServletRequest) request).getServletPath());

        if (!pattern.matcher(header.getMsgTp()).matches()) {
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), "请求报文类型与URL地址不匹配");
        }

        chain.doFilter(request, response);
    }

}
