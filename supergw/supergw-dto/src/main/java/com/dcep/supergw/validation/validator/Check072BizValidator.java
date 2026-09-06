package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc072.Dcep07200101DTO;
import com.dcep.supergw.validation.Check072Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class Check072BizValidator implements ConstraintValidator<Check072Biz, Object> {

    static final String PR00 = "PR00";

    static final String PR01 = "PR01";

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (!(object instanceof Dcep07200101DTO)) {
            return true;
        }

        Dcep07200101DTO dto = (Dcep07200101DTO) object;

        if (PR00.equals(dto.getRspnInf().getRspnSts())) {
            if (dto.getMsgInf() == null) {
                CheckUtils.changeValidatorMsg("业务回执状态为PR00时,验证码内容信息MsgInf必填", context);
                return false;
            }
        }

        if (PR01.equals(dto.getRspnInf().getRspnSts())) {
            if (StringUtils.isBlank(dto.getRspnInf().getRjctCd()) || StringUtils.isBlank(
                dto.getRspnInf().getRjctInf())) {
                CheckUtils.changeValidatorMsg("业务回执状态为PR01时,拒绝码和拒绝信息必填", context);
                return false;
            }
        }

        return true;
    }
}
