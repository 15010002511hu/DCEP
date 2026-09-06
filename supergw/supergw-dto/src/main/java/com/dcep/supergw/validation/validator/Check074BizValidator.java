package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc074.Dcep07400101DTO;
import com.dcep.supergw.validation.Check074Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class Check074BizValidator implements ConstraintValidator<Check074Biz, Object> {

    static final String PR00 = "PR00";

    static final String PR01 = "PR01";

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (!(object instanceof Dcep07400101DTO)) {
            return true;
        }

        Dcep07400101DTO dto = (Dcep07400101DTO) object;

        if (PR00.equals(dto.getRspnInf().getRspnSts())) {
            if (dto.getRspCnt() == null) {
                CheckUtils.changeValidatorMsg("业务回执状态为PR00时,应答内容RspCnt必填", context);
                return false;
            }
        }

        if (PR01.equals(dto.getRspnInf().getRspnSts())) {
            if (StringUtils.isBlank(dto.getRspnInf().getRjctCd()) || null == dto.getRspnInf()) {
                CheckUtils.changeValidatorMsg("业务回执状态为PR01时,拒绝码和拒绝信息必填", context);
                return false;
            }
        }

        return true;
    }
}
