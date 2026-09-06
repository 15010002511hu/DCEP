/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc008.Dcep00800101DTO;
import com.dcep.supergw.validation.Check008Biz;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 008报文校验器
 *
 * @author qinchaoyong
 * @date 2024/12/12 12:38
 */
public class Check008BizValidator implements ConstraintValidator<Check008Biz, Object> {
    /**
     * PR00
     */
    private static final String RSPN_STS_PR00 = "PR00";

    @Override
    public void initialize(Check008Biz constraint) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep00800101DTO)) {
            return true;
        }
        Dcep00800101DTO dto = (Dcep00800101DTO) value;

        // 开始复合字段的逻辑判断
        if (null != dto.getRspnInf() && null != dto.getRspnInf().getRspnSts()) {
            if (RSPN_STS_PR00.equals(dto.getRspnInf().getRspnSts()) && null == dto.getPdctDtl()) {
                CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，合约产品详情信息必填", context);
                return false;
            }
        }

        return true;
    }
}