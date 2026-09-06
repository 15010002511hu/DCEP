package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc369.DCEP36900101DTO;
import com.dcep.supergw.validation.Check369Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : Check369bizValidator.java v 0.1 2021-04-14
 * @description :修改说明2021-09-26：当业务回执状态为“PR00”时，TrxTp必填-----代码回退此校验
 */
public class Check369bizValidator implements ConstraintValidator<Check369Biz, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof DCEP36900101DTO.RspsnInf)) {
            return true;
        }

        DCEP36900101DTO.RspsnInf rspsnInf = (DCEP36900101DTO.RspsnInf) value;

        if ("PR01".equals(rspsnInf.getRspsnSts()) && StringUtils.isBlank(rspsnInf.getRjctCd())) {
            CheckUtils.changeValidatorMsg("业务回执状态为PRO1，业务拒绝码必填", context);
            return false;
        }

        if ("PR00".equals(rspsnInf.getRspsnSts()) && (StringUtils.isBlank(rspsnInf.getTrxFinishTm()) || StringUtils.isBlank(rspsnInf.getBatchId()))) {
            CheckUtils.changeValidatorMsg("业务回执状态为PR00，交易完成时间和交易批次号必填", context);
            return false;
        }

        if ("PR00".equals(rspsnInf.getRspsnSts()) && (StringUtils.isBlank(rspsnInf.getRPFlg()))) {
            CheckUtils.changeValidatorMsg("业务回执状态为PR00，收付标识必填", context);
            return false;
        }
        
        return true;
    }
}
