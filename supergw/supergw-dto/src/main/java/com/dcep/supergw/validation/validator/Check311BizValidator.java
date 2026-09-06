package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc311.Dcep31100101DTO;
import com.dcep.supergw.validation.Check311Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author laimincai
 * @version : Check311BizValidator.java v 0.1 2023-01-05
 * @description : 311校验器
 */
public class Check311BizValidator implements ConstraintValidator<Check311Biz, Object> {

    private static final String RPF02 = "RPF02";

    private static final String RP01 = "RP01";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep31100101DTO)) {
            return true;
        }
        
        Dcep31100101DTO dto = (Dcep31100101DTO) value;

        if (RPF02.equals(dto.getOrgnlGrpHdr().getRPFlg()) && dto.getOrgnlGrpHdr().getOrgnlOutOrdrNo() == null) {
            CheckUtils.changeValidatorMsg("当收付标识为RPF02时,原受理订单号必填", context);
            return false;
        }

        if (RP01.equals(dto.getOrgnlGrpHdr().getRPFlg()) && dto.getOrgnlGrpHdr().getOrgnlOutOrdrNo() == null) {
            CheckUtils.changeValidatorMsg("当收付标识为RP01时,原受理订单号必填", context);
            return false;
        }

        return true;
    }
}
