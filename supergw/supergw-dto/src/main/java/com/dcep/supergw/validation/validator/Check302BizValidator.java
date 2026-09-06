package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc302.Dcep30200101DTO;
import com.dcep.supergw.validation.Check302Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : duzhong
 * @version : Check302BizValidator.java v 0.1 2021-04-15
 * @description :
 */

public class Check302BizValidator implements ConstraintValidator<Check302Biz, Object> {

    private static final String FAILE = "PR01";
    private static final String SUCCESS = "PR02";
    private static final String TT01 = "TT01";
    private static final String TT02 = "TT02";
    private static final String COT01 = "COT01";

    private static final String TT23 = "TT23";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep30200101DTO)) {
            return true;
        }
        Dcep30200101DTO dto = (Dcep30200101DTO) value;

        if (FAILE.equals(dto.getRspsnInf().getRspsnSts()) && StringUtils.isBlank(dto.getRspsnInf().getRjctCd())) {
            CheckUtils.changeValidatorMsg("当业务回执状态为PRO1时,业务拒绝码必填", context);
            return false;
        }

        if (SUCCESS.equals(dto.getRspsnInf().getRspsnSts()) && (StringUtils.isBlank(dto.getRspsnInf().getOrdrTm()) || StringUtils.isBlank(dto.getRspsnInf().getOrdrNo()))) {
            CheckUtils.changeValidatorMsg("当业务回执状态为PRO2时,订单号生成时间和订单号必填", context);
            return false;
        }

        if (SUCCESS.equals(dto.getRspsnInf().getRspsnSts())) {
            if(COT01.equals(dto.getRspsnInf().getCreOrdrTp()) && dto.getqRCodeInf() == null) {
                CheckUtils.changeValidatorMsg("当业务回执状态为PRO2时候且下单类型为COT01时，收款码信息必填", context);
                return false;
            }

            if(StringUtils.isBlank(dto.getRspsnInf().getCreOrdrTp()) &&
                ((TT01.equals(dto.getRspsnInf().getTrxTp())
                    || TT02.equals(dto.getRspsnInf().getTrxTp())))
                && dto.getqRCodeInf() == null) {
                CheckUtils.changeValidatorMsg("当业务回执状态为PRO2时候且交易类型为TT01|TT02时，收款码信息必填", context);
                return false;
            }

            if (StringUtils.isBlank(dto.getRspsnInf().getCreOrdrTp()) &&
                ((TT01.equals(dto.getRspsnInf().getTrxTp())
                    || TT02.equals(dto.getRspsnInf().getTrxTp())))
                && dto.getqRCodeInf() == null) {
                CheckUtils.changeValidatorMsg("当业务回执状态为PRO2时候且交易类型为TT01|TT02时，收款码信息必填",
                    context);
                return false;
            }


            //业务回执类型为PR02,交易类型为TT23,终端支付类型必填
            if (TT23.equals(dto.getRspsnInf().getTrxTp())) {
                if (StringUtils.isBlank(dto.getRspsnInf().getBizPayMtd())) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PRO2时候且交易类型为TT23时，终端支付支持类型必填",
                        context);
                    return false;
                }
            }
        }






        return true;
    }
}
