package com.dcep.supergw.common.enums;/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */

/**
 * 交易参数失效告警类型
 *
 * @author qinchaoyong
 * @date 2024/4/24 11:03
 */
public enum TradeParamWarningType {
    /**
     * 不校验
     */
    NO_CHECK(1, "不校验"),

    /**
     * 校验但不拦截，需要告警
     */
    CHECK_NO_BLOCK(2, "校验但不拦截，需要告警"),

    /**
     * 校验但不拦截，需要告警
     */
    CHECK_AND_BLOCK(3, "校验且拦截，需要告警");

    /**
     * 枚举编码
     */
    private final Integer type;

    /**
     * 描述说明
     */
    private final String desc;

    TradeParamWarningType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
