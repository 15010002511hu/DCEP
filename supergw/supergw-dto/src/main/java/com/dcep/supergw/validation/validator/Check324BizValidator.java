package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc324.Dcep32400101DTO;
import com.dcep.supergw.validation.Check324Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class Check324BizValidator implements ConstraintValidator<Check324Biz, Object> {

    private final static String PR00 = "PR00";
    private final static String PR01 = "PR01";
    private final static String PR02 = "PR02";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep32400101DTO)) {
            return true;
        }

        Dcep32400101DTO dto = (Dcep32400101DTO) value;

        if (PR01.equals(dto.getRspsnInf().getRspsnSts())) {
            if (StringUtils.isBlank(dto.getRspsnInf().getRjctCd())) {
                CheckUtils.changeValidatorMsg("业务回执状态为PR01,业务拒绝码RjctCd必填", context);
                return false;
            }
            if (StringUtils.isBlank(dto.getRspsnInf().getRjctInf())) {
                CheckUtils.changeValidatorMsg("业务回执状态为PR01,业务拒绝信息RjctInf必填", context);
                return false;
            }
        }
        if (PR00.equals(dto.getRspsnInf().getRspsnSts()) || PR02.equals(dto.getRspsnInf().getRspsnSts())) {
            if (StringUtils.isBlank(dto.getRspsnInf().getOrdrNo())) {
                CheckUtils.changeValidatorMsg("业务回执状态为PR00|PR02时,订单号OrdrNo必填", context);
                return false;
            }
        }
        return true;
    }
}
