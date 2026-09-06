/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.controller.filter;

import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.config.BeansCondition;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.record.Timer;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcepex.trace.support.UDT;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author huyajun
 */
@Slf4j
@BeansCondition(name = "beans.controller", havingValue = "MainController")
public class SoapFilter implements Filter {

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        // 设置字符集
        request.setCharacterEncoding(Constant.CHARTSET);
        response.setCharacterEncoding(Constant.CHARTSET);

        // HTTP Header日志输出
        if (log.isDebugEnabled()) {
            printRequestHeader((HttpServletRequest) request);
        }

        // 读取byte流
        byte[] xml = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletInputStream sis = request.getInputStream();
        try {
            int index = -1;
            byte[] bytes = new byte[1024];
            while ((index = sis.read(bytes)) > 0) {
                baos.write(bytes, 0, index);
            }
            xml = baos.toByteArray();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    ;
                }
            }
            if (sis != null) {
                try {
                    sis.close();
                } catch (Exception e) {
                    ;
                }
            }
        }

        // 解析SoapHeader
        SoapHeader header = SoapUtils.getSoapHeaderBean(xml);
        LoggerUtils.mdc(header);

        // 全链路业务监控增加，用于耗时统计
        UDT.addBizData(header);

        // 设置上下文根
        request.setAttribute("beginTime", Timer.currentTime());
        request.setAttribute("header", header);
        request.setAttribute("xml", xml);
        chain.doFilter(request, response);
    }

    private void printRequestHeader(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            headerMap.put(name, value);
        }
        log.debug("收到HTTP请求Header: {}", headerMap.keySet().stream().map(key -> key + "=" + headerMap.get(key))
            .collect(Collectors.joining(",", "{", "}")));
    }
}
