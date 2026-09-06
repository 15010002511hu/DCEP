package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc306.Dcep30600101DTO;
import com.dcep.supergw.validation.Check306Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author weiqianjing
 * @Date 2021/9/26 18:28
 * @Description：新增订单号字段
 */
public class Check306BizValidator implements ConstraintValidator<Check306Biz, Object> {
    /**
     * 业务处理成功
     */
    private static final String PR00 = "PR00";

    /**
     * 业务处理失败
     */
    private static final String PR01 = "PR01";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Dcep30600101DTO dto = (Dcep30600101DTO) value;

        if ((PR01.equals(dto.getRspsnInf().getRspsnSts()) && (dto.getRspsnInf().getRjctCd() == null))) {
            CheckUtils.changeValidatorMsg("当业务回执状态RspsnSts值为PR01失败,业务拒绝码RjctCd必填", context);
            return false;
        }
        if (PR00.equals(dto.getRspsnInf().getRspsnSts())) {
            if (StringUtils.isBlank(dto.getRspsnInf().getOrdrNo())) {
                CheckUtils.changeValidatorMsg("当业务回执状态RspsnSts值为PR00成功,订单号OrdrNo必填", context);
                return false;
            }
            if (StringUtils.isBlank(dto.getRspsnInf().getOrdrTm())) {
                CheckUtils.changeValidatorMsg("当业务回执状态RspsnSts值为PR00成功,订单生成时间OrdrTm必填", context);
                return false;
            }
            if (StringUtils.isBlank(dto.getRspsnInf().getTrxFinishTm())) {
                CheckUtils.changeValidatorMsg("当业务回执状态RspsnSts值为PR00成功,交易完成时间TrxFinishTm必填", context);
                return false;
            }
            if (StringUtils.isBlank(dto.getRspsnInf().getBatchId())) {
                CheckUtils.changeValidatorMsg("当业务回执状态RspsnSts值为PR00成功,交易批次号BatchId必填", context);
                return false;
            }
        }


        return true;
    }
}
