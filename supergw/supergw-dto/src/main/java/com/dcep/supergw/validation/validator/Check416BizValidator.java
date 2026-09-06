package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc416.Dcep41600101DTO;
import com.dcep.supergw.validation.Check416Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import jodd.util.StringUtil;

/**
 * @author : maxinyu
 * @version : Check416BizValidator.java v 0.1 2019-11-12
 * @description :
 */
public class Check416BizValidator implements ConstraintValidator<Check416Biz, Object> {

    private static final String PR00 = "PR00";
    private static final String PR01 = "PR01";
    private static final String PR02 = "PR02";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Dcep41600101DTO dto = (Dcep41600101DTO) value;

        String rs = dto.getBizQryRef().getQryRs();
        if (PR00.equals(rs)) {
            if (dto.getBizRpt() == null) {
                CheckUtils.changeValidatorMsg("当QryRs为PR00时BizRpt不能为空", context);
                return false;
            }

        } else if (PR01.equals(rs)) {
            if(dto.getOprlErr() == null||dto.getOprlErr().getErr().getRjctCd()==null){
                CheckUtils.changeValidatorMsg("当QryRs为PR01时OprlErr不能为空", context);
                return false;
            }

        }

        if (PR02.equals(rs)) {
            if(dto.getBizRpt()==null||dto.getBizRpt().getCdtr()==null|| StringUtil.isBlank(dto.getBizRpt().getCdtr().getResdtCtryCd())){
                CheckUtils.changeValidatorMsg("当QryRs为PR02时ResdtCtryCd不能为空", context);
                return false;
            }
        }


        return true;
    }
}
