/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc028.Dcep02800101DTO;
import com.dcep.supergw.validation.Check028Biz;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 028报文校验器
 *
 * @author qinchaoyong
 * @date 2024/12/12 12:38
 */
public class Check028BizValidator implements ConstraintValidator<Check028Biz, Object> {
    /**
     * PR00
     */
    private static final String RSPN_STS_PR00 = "PR00";

    @Override
    public void initialize(Check028Biz constraint) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep02800101DTO)) {
            return true;
        }
        Dcep02800101DTO dto = (Dcep02800101DTO) value;

        // 开始复合字段的逻辑判断
        if (null != dto.getRspnInf() && null != dto.getRspnInf().getRspnSts()) {
            if (RSPN_STS_PR00.equals(dto.getRspnInf().getRspnSts()) &&
                    null == dto.getPdctCretAudtDtlInf()) {
                CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，合约产品创建审核记录详情查询信息必填", context);
                return false;
            }
        }

        return true;
    }
}