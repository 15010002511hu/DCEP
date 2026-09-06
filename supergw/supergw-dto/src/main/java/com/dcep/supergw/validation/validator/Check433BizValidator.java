package com.dcep.supergw.validation.validator;

import com.dcep.supergw.dto.dc433.Dcep43300101DTO;
import com.dcep.supergw.validation.Check433Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : maxinyu
 * @version : Check433BizValidator.java v 0.1 2019-09-09
 * @description :
 */
public class Check433BizValidator implements ConstraintValidator<Check433Biz, Object> {
    final String MT03 = "MT03";
    final String SG00 = "SG00";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Dcep43300101DTO dto = (Dcep43300101DTO) value;

        if (dto.getMgmtTp().equals(MT03)) {
            return SG00.equals(dto.getSgnInf().getSgnTp());
        }
        return true;
    }
}
