/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc014.Dcep01400101DTO;
import com.dcep.supergw.dto.dc016.Dcep01600101DTO;
import com.dcep.supergw.validation.Check016Biz;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 016报文校验器
 *
 * @author qinchaoyong
 * @date 2024/12/12 12:38
 */
public class Check016BizValidator implements ConstraintValidator<Check016Biz, Object> {
    /**
     * PR00
     */
    private static final String RSPN_STS_PR00 = "PR00";

    @Override
    public void initialize(Check016Biz constraint) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep01400101DTO)) {
            return true;
        }
        Dcep01600101DTO dto = (Dcep01600101DTO) value;

        // 开始复合字段的逻辑判断
        if (null != dto.getRspnInf() && null != dto.getRspnInf().getRspnSts()) {
            if (RSPN_STS_PR00.equals(dto.getRspnInf().getRspnSts())) {
                if (null == dto.getRspnInf().getPdctId()) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，合约产品ID必填", context);
                    return false;
                }

                if (null == dto.getRspnInf().getRspnPgInf()) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，应答分页信息必填", context);
                    return false;
                }
            }
        }

        return true;
    }
}