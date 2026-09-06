/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.manager.https.interceptor;

import com.dcep.common.Constants.CommonConstant;
import com.dcep.supergw.common.constant.Constant;
import com.dcepex.trace.support.TraceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.concurrent.FutureCallback;

/**
 * HTTP异步出向访问机构环境位设置
 *
 * @author laimincai
 * @date 2023/11/09
 */
@Slf4j
public class EnvFlagHttpCallBack implements FutureCallback<HttpResponse> {

    private final FutureCallback<HttpResponse> callback;

    public EnvFlagHttpCallBack(FutureCallback<HttpResponse> callback) {
        this.callback = callback;
    }

    /**
     * 设计原则： 考虑DCEP报文交互模式均为同步模式，全链路Tracer的环境位信息以请求报文为准，应答报文的环境位信息仅在此做校验和日志告警
     */
    @Override
    public void completed(HttpResponse result) {
        if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 仅当HTTP200情况下校验应答报文头中的环境位信息
            String requestLoadTraffic = TraceContext.get(CommonConstant.LOAD_TRAFFIC_KEY);
            String responseLoadTraffic = null;
            if (result.getLastHeader(CommonConstant.LOAD_TRAFFIC_KEY) != null) {
                responseLoadTraffic = result.getLastHeader(CommonConstant.LOAD_TRAFFIC_KEY).getValue();
                if (responseLoadTraffic != null && !responseLoadTraffic.equals(requestLoadTraffic)) {
                    log.warn("请求报文LoadTraffic:{}与应答报文LoadTraffic:{}不一致", requestLoadTraffic, responseLoadTraffic);
                }
            }

            if (responseLoadTraffic == null && Constant.LOAD_TRAFFIC_SWITCH_TRUE.equals(requestLoadTraffic)) {
                log.warn("机构应答的压测流量标识LoadTraffic与msgId环境标识位不匹配");
            }
        }

        callback.completed(result);
    }

    @Override
    public void failed(Exception ex) {
        callback.failed(ex);
    }

    @Override
    public void cancelled() {
        callback.cancelled();
    }
}
