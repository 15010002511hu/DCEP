package com.dcep.supergw.validation.validator;

import com.dcep.supergw.dto.dc433.Dcep43300101DTO;
import com.dcep.supergw.validation.Check433Biz1;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : Check433BizValidator1.java v 0.1 2020-03-19
 * @description :
 */
public class Check433BizValidator1 implements ConstraintValidator<Check433Biz1, Object> {
    final String MT03 = "MT03";
    final String SG00 = "SG00";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Dcep43300101DTO dto = (Dcep43300101DTO) value;
        // 当管理类型mgmtTp为MT03时 且 签约类型同时为SG00 时候，挂接协议号必填
        if (MT03.equals(dto.getMgmtTp()) && SG00.equals(dto.getSgnInf().getSgnTp())) {
            return !StringUtils.isBlank(dto.getPtcInf().getPtcId());
        }
        return true;
    }
}
