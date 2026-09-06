/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc036.Dcep03600101DTO;
import com.dcep.supergw.validation.Check036Biz;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 036报文校验器
 *
 * @author qinchaoyong
 * @date 2024/12/12 12:38
 */
public class Check036BizValidator implements ConstraintValidator<Check036Biz, Object> {
    /**
     * PR00
     */
    private static final String RSPN_STS_PR00 = "PR00";

    @Override
    public void initialize(Check036Biz constraint) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep03600101DTO)) {
            return true;
        }
        Dcep03600101DTO dto = (Dcep03600101DTO) value;

        // 开始复合字段的逻辑判断
        if (null != dto.getRspnInf() && null != dto.getRspnInf().getRspnSts()) {
            if (RSPN_STS_PR00.equals(dto.getRspnInf().getRspnSts())) {
                if (null == dto.getRspnInf().getChckRspnInf()) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，签约方信息验证结果必填", context);
                    return false;
                }
            }
        }

        return true;
    }
}