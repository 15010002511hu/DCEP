package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc316.DCEP31600101DTO;
import com.dcep.supergw.validation.Check316Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : Check316bizValidator.java v 0.1 2021-04-14
 * @description :
 */
public class Check316bizValidator implements ConstraintValidator<Check316Biz, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof DCEP31600101DTO.RspsnInf)) {
            return true;
        }

        DCEP31600101DTO.RspsnInf rspsnInf = (DCEP31600101DTO.RspsnInf) value;

        if ("PR01".equals(rspsnInf.getRspsnSts()) && StringUtils.isBlank(rspsnInf.getRjctCd())) {
            CheckUtils.changeValidatorMsg("业务回执状态为PRO1，业务拒绝码必填", context);
            return false;
        }

        return true;
    }
}
