/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc426.Dcep42600101DTO;
import com.dcep.supergw.validation.Check426Biz;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 426报文校验器
 *
 * @author qinchaoyong
 * @date 2025/03/17 16:54
 */
public class Check426BizValidator implements ConstraintValidator<Check426Biz, Object> {
    /**
     * PR01
     */
    private static final String STS_ID_PR01 = "PR01";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (!(value instanceof Dcep42600101DTO)) {
            return true;
        }
        Dcep42600101DTO dto = (Dcep42600101DTO) value;

        // 开始复合字段的逻辑判断
        if (STS_ID_PR01.equals(dto.getRspsnInf().getRspsnSts())
                && (null == dto.getRspsnInf().getRjctCd() || null == dto.getRspsnInf().getRjctInf())) {
            CheckUtils.changeValidatorMsg("当业务回执状态为PR01时，业务拒绝码和业务拒绝信息必填", constraintValidatorContext);
            return false;
        }

        //最后都满足时返回true
        return true;
    }
}
