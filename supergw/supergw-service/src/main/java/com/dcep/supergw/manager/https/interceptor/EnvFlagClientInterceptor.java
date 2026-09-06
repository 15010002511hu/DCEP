/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.manager.https.interceptor;

import com.dcep.common.Constants.CommonConstant;
import com.dcep.supergw.common.constant.Constant;
import com.dcepex.trace.support.TraceContext;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HttpContext;

/**
 * HTTP出向访问机构环境位设置
 *
 * @author laimincai
 * @date 2023/10/30
 */
@Slf4j
public class EnvFlagClientInterceptor implements HttpRequestInterceptor, HttpResponseInterceptor {

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        request.setHeader(CommonConstant.LOAD_TRAFFIC_KEY, TraceContext.get(CommonConstant.LOAD_TRAFFIC_KEY));
    }

    /**
     * 设计原则： 考虑DCEP报文交互模式均为同步模式，全链路Tracer的环境位信息以请求报文为准，应答报文的环境位信息仅在此做校验和日志告警
     */
    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 仅当HTTP200情况下校验应答报文头中的环境位信息
            String requestLoadTraffic = TraceContext.get(CommonConstant.LOAD_TRAFFIC_KEY);
            String responseLoadTraffic = null;
            if (response.getLastHeader(CommonConstant.LOAD_TRAFFIC_KEY) != null) {
                responseLoadTraffic = response.getLastHeader(CommonConstant.LOAD_TRAFFIC_KEY).getValue();
                if (responseLoadTraffic != null && !responseLoadTraffic.equals(requestLoadTraffic)) {
                    log.warn("请求报文LoadTraffic:{}与应答报文LoadTraffic:{}不一致", requestLoadTraffic, responseLoadTraffic);
                }
            }

            if (responseLoadTraffic == null && Constant.LOAD_TRAFFIC_SWITCH_TRUE.equals(requestLoadTraffic)) {
                log.warn("机构应答报文头的压测流量标识LoadTraffic与msgId环境标识位不匹配");
            }
        }
    }
}
