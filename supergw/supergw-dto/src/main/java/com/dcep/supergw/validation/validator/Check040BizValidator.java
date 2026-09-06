/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc040.Dcep04000101DTO;
import com.dcep.supergw.validation.Check040Biz;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 040报文校验器
 *
 * @author qinchaoyong
 * @date 2024/12/12 12:38
 */
public class Check040BizValidator implements ConstraintValidator<Check040Biz, Object> {
    /**
     * PR00
     */
    private static final String RSPN_STS_PR00 = "PR00";

    @Override
    public void initialize(Check040Biz constraint) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep04000101DTO)) {
            return true;
        }
        Dcep04000101DTO dto = (Dcep04000101DTO) value;

        // 开始复合字段的逻辑判断
        if (null != dto.getRspnInf() && null != dto.getRspnInf().getRspnSts()) {
            if (RSPN_STS_PR00.equals(dto.getRspnInf().getRspnSts())) {
                if (null == dto.getCtrctSgntrRcrd()) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，合约签约记录必填", context);
                    return false;
                }
            }
        }

        return true;
    }
}