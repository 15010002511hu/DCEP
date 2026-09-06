package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc326.Dcep32600101DTO;
import com.dcep.supergw.validation.Check326Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Check326bizValidator implements ConstraintValidator<Check326Biz, Object> {

    private final static String PR00 = "PR00";
    private final static String PR01 = "PR01";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep32600101DTO)) {
            return true;
        }

        Dcep32600101DTO dto = (Dcep32600101DTO) value;

        if (PR00.equals(dto.getBizQryRef().getQryRs()) && dto.getBizInf()==null) {
            CheckUtils.changeValidatorMsg("当查询处理状态为PR00时,查询响应信息必填", context);
            return false;
        }
        if(PR01.equals(dto.getBizQryRef().getQryRs()) && dto.getOprlErr()==null){
            CheckUtils.changeValidatorMsg("当查询处理状态为PR01时，应答拒绝信息必填", context);
            return false;
        }

        return true;
    }
}
