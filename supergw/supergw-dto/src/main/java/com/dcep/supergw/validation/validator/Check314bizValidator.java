package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc314.DCEP31400101DTO;
import com.dcep.supergw.validation.Check314Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : maxinyu
 * @version : Check314bizValidator.java v 0.1 2021-04-14
 * @description :
 */
public class Check314bizValidator implements ConstraintValidator<Check314Biz, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        /**
         * 若注解没用314Dto上，则直接返回true
         */
        if (!(value instanceof DCEP31400101DTO)) {
            return true;
        }

        DCEP31400101DTO obj = (DCEP31400101DTO) value;

        if ("PR00".equals(obj.getBizQryRef().getQryRs()) && obj.getBizRpt() == null) {
            CheckUtils.changeValidatorMsg("查询处理状态为PR00,原业务应答信息必填", context);
            return false;
        }
        
        if ("PR00".equals(obj.getBizQryRef().getQryRs()) && obj.getOprlErr() != null) {
            CheckUtils.changeValidatorMsg("查询处理状态为PR00,应答拒绝信息不需填写", context);
            return false;
        }
        if ("PR01".equals(obj.getBizQryRef().getQryRs()) && obj.getOprlErr() == null) {
            CheckUtils.changeValidatorMsg("查询处理状态为PR01,应答拒绝信息必填", context);
            return false;
        }
        if ("PR01".equals(obj.getBizQryRef().getQryRs()) && obj.getBizRpt() != null) {
            CheckUtils.changeValidatorMsg("查询处理状态为PR00,原业务应答信息不需填写", context);
            return false;
        }

        if (obj.getBizRpt() != null && "PR01".equals(obj.getBizRpt().getTrnRs()) && obj.getBizRpt().getRsn().getRjctCd() == null) {
            CheckUtils.changeValidatorMsg("原业务状态为PR01,业务拒绝码必填", context);
            return false;
        }

        if (obj.getBizRpt() != null && "PR00".equals(obj.getBizRpt().getTrnRs()) && (obj.getBizRpt().getOrgnlTxInf().getOrgnlBatchId() == null || obj.getBizRpt().getOrgnlTxInf().getOrgnlTrxFinishTm() == null)) {
            CheckUtils.changeValidatorMsg("原业务状态为PR00,原交易批次号和原交易完成时间必填", context);
            return false;
        }


        return true;

    }
}
