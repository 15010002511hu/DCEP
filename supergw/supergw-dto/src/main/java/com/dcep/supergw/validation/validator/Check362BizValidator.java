package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc362.Dcep362001001;
import com.dcep.supergw.validation.Check362Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : Check362BizValidator.java v 0.1 2023-01-06
 * Copyright 2023 PBCDCI ALL Rights
 * @description :
 */
public class Check362BizValidator implements ConstraintValidator<Check362Biz, Object> {
    private final static String PR00 = "PR00";
    private final static String PR01 = "PR01";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep362001001)) {
            return true;
        }

        Dcep362001001 dto = (Dcep362001001) value;

        if (PR01.equals(dto.getRspsnInf().getRspsnSts())) {
            if (StringUtils.isBlank(dto.getRspsnInf().getRjctCd())) {
                CheckUtils.changeValidatorMsg("业务回执状态为PRO1，业务拒绝码RjctCd必填", context);
                return false;
            } else if (StringUtils.isBlank(dto.getRspsnInf().getRjctInf())) {
                CheckUtils.changeValidatorMsg("业务回执状态为PRO1，业务拒绝信息RjctInf必填", context);
                return false;
            }
        }
        return true;
    }
}
