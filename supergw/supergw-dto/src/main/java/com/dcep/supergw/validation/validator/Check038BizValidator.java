/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc038.Dcep03800101DTO;
import com.dcep.supergw.validation.Check038Biz;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 038报文校验器
 *
 * @author qinchaoyong
 * @date 2024/12/12 12:38
 */
public class Check038BizValidator implements ConstraintValidator<Check038Biz, Object> {
    /**
     * PR00
     */
    private static final String RSPN_STS_PR00 = "PR00";

    @Override
    public void initialize(Check038Biz constraint) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep03800101DTO)) {
            return true;
        }
        Dcep03800101DTO dto = (Dcep03800101DTO) value;

        // 开始复合字段的逻辑判断
        if (null != dto.getRspnInf() && null != dto.getRspnInf().getRspnSts()) {
            if (RSPN_STS_PR00.equals(dto.getRspnInf().getRspnSts())) {
                if (null == dto.getRspnInf().getCtrctSgntrRcrd()) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，签约记录必填", context);
                    return false;
                }
            }
        }

        return true;
    }
}