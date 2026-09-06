package com.dcep.supergw.validation.validator;

import com.dcep.supergw.dto.dc434.Dcep43400101DTO;
import com.dcep.supergw.validation.Check434Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : maxinyu
 * @version : Check434BizValidator.java v 0.1 2019-09-09
 * @description :
 */
public class Check434BizValidator implements ConstraintValidator<Check434Biz, Object> {
    final String PR00 = "PR00";
    final String MT02 = "MT02";
    final String SG01 = "SG01";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Dcep43400101DTO dto = (Dcep43400101DTO) value;

        if (PR00.equals(dto.getRspsInf().getRspsnSts())) {
            if (MT02.equals(dto.getRspsInf().getMgmtTp())) {
                return SG01.equals(dto.getSgnInf().getSgnTp());
            }
        }
        return true;

    }
}
