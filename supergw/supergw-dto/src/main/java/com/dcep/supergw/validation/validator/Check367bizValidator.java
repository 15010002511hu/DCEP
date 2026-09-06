package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc367.DCEP36700101DTO;
import com.dcep.supergw.validation.Check367Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : Check367bizValidator.java v 0.1 2021-04-14
 * @description :
 */
public class Check367bizValidator implements ConstraintValidator<Check367Biz, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof DCEP36700101DTO.RspsnInf)) {
            return true;
        }

        DCEP36700101DTO.RspsnInf rspsnInf = (DCEP36700101DTO.RspsnInf) value;

        if ("PR01".equals(rspsnInf.getRspsnSts()) && StringUtils.isBlank(rspsnInf.getRjctCd())) {
            CheckUtils.changeValidatorMsg("业务回执状态为PRO1，业务拒绝码必填", context);
            return false;
        }

        if ("PR00".equals(rspsnInf.getRspsnSts()) && (StringUtils.isBlank(rspsnInf.getTrxFinishTm()) || StringUtils.isBlank(rspsnInf.getBatchId()))) {
            CheckUtils.changeValidatorMsg("业务回执状态为PRO0，交易完成和交易批次号必填", context);
            return false;
        }

        return true;
    }
}
