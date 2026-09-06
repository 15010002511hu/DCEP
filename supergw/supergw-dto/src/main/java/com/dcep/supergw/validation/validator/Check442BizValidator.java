package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc442.Dcep44200101DTO;
import com.dcep.supergw.validation.Check442Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : maxinyu
 * @version : Check434BizValidator.java v 0.1 2019-09-09
 * @description :
 */
public class Check442BizValidator implements ConstraintValidator<Check442Biz, Object> {


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Dcep44200101DTO dto = (Dcep44200101DTO) value;


        //当管理类型为“MT06”，且业务回执状态为“PR00”时ChnlInf必填写
        if("MT06".equals(dto.getMgmtTp()) && "PR00".equals(dto.getRspsnInf().getRspsnSts()) && dto.getChnlInf()==null) {
            CheckUtils.changeValidatorMsg("当管理类型为“MT06”，且业务回执状态为“PR00”时ChnlInf必填写", context);
            return false;
        }
        //当管理类型为“MT05”，且业务回执状态为“PR00”时RdrctInf必填写
        else if("MT05".equals(dto.getMgmtTp()) && "PR00".equals(dto.getRspsnInf().getRspsnSts()) && dto.getRdrctInf()==null){
            CheckUtils.changeValidatorMsg("当管理类型为“MT05”，且业务回执状态为“PR00”时RdrctInf必填写", context);
            return false;
        }

        return true;

    }
}
