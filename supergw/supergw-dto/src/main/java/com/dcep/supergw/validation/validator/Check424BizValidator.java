/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc424.Dcep42400101DTO;
import com.dcep.supergw.validation.Check424Biz;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 422报文校验器
 *
 * @author qinchaoyong
 * @date 2025/01/15 17:24
 */
public class Check424BizValidator implements ConstraintValidator<Check424Biz, Object> {

    /**
     * PR00：成功
     */
    private static final String RSPSN_STS_PR00 = "PR00";

    /**
     * PR01：失败
     */
    private static final String RSPSN_STS_PR01 = "PR01";

    @Override
    public void initialize(Check424Biz constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep42400101DTO)) {
            return true;
        }

        Dcep42400101DTO dto = (Dcep42400101DTO) value;

        // 查询状态不能既成功又失败
        if (RSPSN_STS_PR01.equals(dto.getRspsnInf().getRspsnSts())) {
            if (StringUtils.isBlank(dto.getRspsnInf().getRjctCd())) {
                CheckUtils.changeValidatorMsg("原交易处理失败时，RjctCd不能为空", context);
                return false;
            }

            if (StringUtils.isBlank(dto.getRspsnInf().getRjctInf())) {
                CheckUtils.changeValidatorMsg("原交易处理失败时，RjctInf不能为空", context);
                return false;
            }
        }

        if (RSPSN_STS_PR00.equals(dto.getRspsnInf().getRspsnSts())) {
            if (StringUtils.isBlank(dto.getRspsnInf().getPhneNbHash())) {
                CheckUtils.changeValidatorMsg("当业务回执状态为PR00时必填，PhneNbHash不能为空", context);
                return false;
            }
        }

        return true;
    }
}
