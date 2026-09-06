/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc059.Dcep05900101DTO;
import com.dcep.supergw.validation.Check059Biz;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 059报文校验器
 *
 * @author qinchaoyong
 * @date 2024/11/22 9:54
 */
public class Check059BizValidator implements ConstraintValidator<Check059Biz, Object> {

    /**
     * 业务类型编码，100：消费
     */
    private static final String BIZ_CTG_CD_100 = "100";

    /**
     * PR00
     */
    private static final String STS_ID_PR00 = "PR00";

    /**
     * PR01
     */
    private static final String STS_ID_PR01 = "PR01";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (!(value instanceof Dcep05900101DTO)) {
            return true;
        }
        Dcep05900101DTO dto = (Dcep05900101DTO) value;

        // 开始复合字段的逻辑判断
        // 业务类型为100时PmtTxId必填
        if (BIZ_CTG_CD_100.equals(dto.getNtfctnInf().getBizCtgyCd())
                && StringUtils.isBlank(dto.getNtfctnInf().getPmtTxId())) {
            CheckUtils.changeValidatorMsg("当业务类型为100消费时支付订单必填", constraintValidatorContext);
            return false;
        }

        if (STS_ID_PR01.equals(dto.getNtfctnInf().getStsId())
                && (null == dto.getNtfctnInf().getRjctCd() || null == dto.getNtfctnInf().getRjctInf())) {
            CheckUtils.changeValidatorMsg("当业务回执状态为PR01时，业务拒绝码和业务拒绝信息必填", constraintValidatorContext);
            return false;
        }

        if (STS_ID_PR00.equals(dto.getNtfctnInf().getStsId()) && null == dto.getNtfctnInf().getCtrctRspn()) {
            CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，合约应答必填", constraintValidatorContext);
            return false;
        }

        //最后都满足时返回true
        return true;
    }
}
