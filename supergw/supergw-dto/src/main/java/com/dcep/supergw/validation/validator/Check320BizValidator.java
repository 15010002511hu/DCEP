package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc320.Dcep32000101DTO;
import com.dcep.supergw.validation.Check320Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : Check320BizValidator.java v 0.1 2023-01-05
 * Copyright 2023 PBCDCI ALL Rights
 * @description :
 */
public class Check320BizValidator implements ConstraintValidator<Check320Biz, Object> {
    /**
     * 业务处理成功
     */
    private final static String PR00 = "PR00";
    /**
     * 业务处理失败
     */
    private final static String PR01 = "PR01";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep32000101DTO)) {
            return true;
        }

        Dcep32000101DTO dcep32000101DTO = (Dcep32000101DTO) value;

        if (PR01.equals(dcep32000101DTO.getRspsnInf().getRspsnSts())) {
            if (StringUtils.isBlank(dcep32000101DTO.getRspsnInf().getRjctCd())) {
                CheckUtils.changeValidatorMsg("业务回执状态为PRO1,业务拒绝码RjctCd必填", context);
                return false;
            }
            if (StringUtils.isBlank(dcep32000101DTO.getRspsnInf().getRjctInf())) {
                CheckUtils.changeValidatorMsg("业务回执状态为PRO1,业务拒绝信息RjctInf必填", context);
                return false;
            }
        }
        if (PR00.equals(dcep32000101DTO.getRspsnInf().getRspsnSts())) {
            if (StringUtils.isBlank(dcep32000101DTO.getRspsnInf().getPmtCode())) {
                CheckUtils.changeValidatorMsg("业务回执状态为PRO0,付款码PmtCode必填", context);
                return false;
            }
            if (StringUtils.isBlank(dcep32000101DTO.getRspsnInf().getPmtCode())) {
                CheckUtils.changeValidatorMsg("业务回执状态为PRO0,付款码有效期PmtCodeExp必填", context);
                return false;
            }
        }
        return true;
    }
}
