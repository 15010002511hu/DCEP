package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc312.Dcep31200101DTO;
import com.dcep.supergw.validation.Check312Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * @author : duzhong
 * @version : Check312BizValidator.java v 0.1 2021-04-15
 * @description :
 */

public class Check312BizValidator implements ConstraintValidator<Check312Biz, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Dcep31200101DTO dto = (Dcep31200101DTO) value;

       /**
        * @Author weiqianjing
        * @date 2022/1/19
        * @Dscription:优化空指针报错问题
        * */
        if ("PR00".equals(dto.getBizQryRef().getQryRs()) && dto.getBizRpt() == null) {
            CheckUtils.changeValidatorMsg("查询处理状态为PR00时填写应答的原业务信息BizRpt", context);
            return false;
        }
        if ("PR00".equals(dto.getBizQryRef().getQryRs())) {
            if ("PR01".equals(dto.getBizRpt().getTrnRs()) && (dto.getBizRpt().getRsn() == null || dto.getBizRpt().getRsn().getRjctCd() == null)) {
                CheckUtils.changeValidatorMsg("当原业务状态为 “PR01”时，业务拒绝码RjctCd必填", context);
                return false;
            }

            if ("PR00".equals(dto.getBizRpt().getTrnRs()) && dto.getBizRpt().getOrgnlTxInf() != null
                    && ((dto.getBizRpt().getOrgnlTxInf().getOrgnlBatchId() == null)
                    || (dto.getBizRpt().getOrgnlTxInf().getOrgnlTrxFinishTm() == null))) {
                CheckUtils.changeValidatorMsg("当原业务状态为 “PR00”时，原交易完成时间OrgnlTrxFinishTm和 原交易批次号OrgnlBatchId必填", context);
                return false;
            }
        }


        if ("PR00".equals(dto.getBizQryRef().getQryRs()) && dto.getOprlErr() != null) {
            CheckUtils.changeValidatorMsg("查询处理状态为PR00时不需要填写OperationalError应答拒绝信息", context);
            return false;
        }

        if ("PR01".equals(dto.getBizQryRef().getQryRs()) && dto.getOprlErr() == null) {
            CheckUtils.changeValidatorMsg("当查询处理状态为PR01时填写OperationalError应答拒绝信息", context);
            return false;
        }
        if ("PR01".equals(dto.getBizQryRef().getQryRs()) && dto.getBizRpt() != null) {
            CheckUtils.changeValidatorMsg("当查询处理状态为PR01时不需要填写应答的原业务信息BizRpt", context);
            return false;
        }
        return true;
    }
}
