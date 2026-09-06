package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc318.DCEP31800101DTO;
import com.dcep.supergw.validation.Check318Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : Check318bizValidator.java v 0.1 2021-04-15
 * @description :
 */
public class Check318bizValidator implements ConstraintValidator<Check318Biz, Object> {

    private final static String PR00 = "PR00";
    private final static String PR01 = "PR01";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof DCEP31800101DTO.RspsnInf)) {
            return true;
        }

        DCEP31800101DTO.RspsnInf rspsnInf = (DCEP31800101DTO.RspsnInf) value;

        if (PR01.equals(rspsnInf.getRspsnSts()) && StringUtils.isBlank(rspsnInf.getRjctCd())) {
            CheckUtils.changeValidatorMsg("业务回执状态为PRO1，业务拒绝码必填", context);
            return false;
        }
        if(PR00.equals(rspsnInf.getRspsnSts()) && StringUtils.isBlank(rspsnInf.getQrCode())){
            CheckUtils.changeValidatorMsg("业务回执状态为PRO0，收款码必填", context);
            return false;
        }

        return true;
    }
}
