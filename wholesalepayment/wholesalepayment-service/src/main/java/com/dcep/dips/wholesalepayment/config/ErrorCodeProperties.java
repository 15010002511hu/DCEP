/*
 * pbcdci.cn Inc.
 * Copyright © 2022 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * DCEP错误码、货币桥错误码映射关系
 * @author wxy
 * @version $Id: ErrorCodeProperties.java, v 0.1 2022年6月6日 下午7:59:49 wxy Exp $
 */
@Slf4j
@ConfigurationProperties(prefix = "errorcode-map")
@Component
public class ErrorCodeProperties {

    /** 货币桥->互联互通DCEP错误码映射MAP */
    private static Map<String, DcepErrInfo> mcbs;

    /** 互联互通DCEP->货币桥错误码映射MAP */
    private static Map<String, McbsErrInfo> dcep;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DcepErrInfo {
        private String dcepCode;
        private String dcepMsg;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class McbsErrInfo {
        private String mcbsCode;
        private String mcbsMsg;
    }

    /**
     * 获取货币桥->DCEP错误码映射MAP，支持普通类调用
     * @return 错误码映射MAP
     */
    public static Map<String, DcepErrInfo> getMcbs() {
        return mcbs;
    }

    /**
     * 获取DCEP->货币桥错误码映射MAP，支持普通类调用
     * @return 错误码映射MAP
     */
    public static Map<String, McbsErrInfo> getDcep() {
        return dcep;
    }

    public void setMcbs(Map<String, DcepErrInfo> mcbs) {
        // spring注入
        synchronized (ErrorCodeProperties.class) {
            ErrorCodeProperties.mcbs = mcbs;
        }
        log.info("货币桥->互联互通错误码映射条目数：{}", mcbs.size());
    }

    public void setDcep(Map<String, McbsErrInfo> dcep) {
        // spring注入
        synchronized (ErrorCodeProperties.class) {
            ErrorCodeProperties.dcep = dcep;
        }
        log.info("互联互通->货币桥错误码映射条目数：{}", dcep.size());
    }

}
