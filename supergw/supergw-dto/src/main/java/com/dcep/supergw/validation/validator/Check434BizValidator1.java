package com.dcep.supergw.validation.validator;

import com.dcep.supergw.dto.dc434.Dcep43400101DTO;
import com.dcep.supergw.validation.Check434Biz1;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : Check434BizValidator1.java v 0.1 2020-03-19
 * @description :
 */
public class Check434BizValidator1 implements ConstraintValidator<Check434Biz1, Object> {
    final String PR00 = "PR00";
    final String MT02 = "MT02";
    final String SG01 = "SG01";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Dcep43400101DTO dto = (Dcep43400101DTO) value;

        if (PR00.equals(dto.getRspsInf().getRspsnSts())) {
            if (MT02.equals(dto.getRspsInf().getMgmtTp()) && SG01.equals(dto.getSgnInf().getSgnTp())) {
                return !StringUtils.isBlank(dto.getRspsInf().getPtcId());
            }
        }
        return true;
    }
}
