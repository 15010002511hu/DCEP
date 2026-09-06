package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc305.Dcep30500101DTO;
import com.dcep.supergw.validation.Check305Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : Check305BizValidator.java v 0.1 2022-10-18
 * Copyright 2022 PBCDCI ALL Rights
 * @description :
 */
public class Check305BizValidator implements ConstraintValidator<Check305Biz, Object> {

    private static final String TT11 = "TT11";
    private static final String MP01 = "MP01";
    private static final String MP02 = "MP02";
    private static final String MP03 = "MP03";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Dcep30500101DTO dto = (Dcep30500101DTO) value;

        if (TT11.equals(dto.getTrxInf().getTrxTp()) && dto.getSgnInf() == null) {
            CheckUtils.changeValidatorMsg(
                    "当交易类型为TT11时,签约人信息必填",
                    context);
            return false;
        }
        if (TT11.equals(dto.getTrxInf().getTrxTp()) && (dto.getCdtrInf().getMrchntIdTp() == null||dto.getCdtrInf().getSceneId()==null||dto.getCdtrInf().getMrchntIdNo()==null)) {
            CheckUtils.changeValidatorMsg(
                    "当交易类型为TT11时,商户证件类型,商户证件编码,场景id必填",
                    context);
            return false;
        }
        if (dto.getCdtrInf() != null
                && (MP01.equals(dto.getCdtrInf().getMrchntPrprty()) || MP03.equals(dto.getCdtrInf().getMrchntPrprty()))
                && (dto.getMrchntTerInf() == null || StringUtils.isBlank(dto.getMrchntTerInf().getMrchntBizAddr()))) {
            CheckUtils.changeValidatorMsg("当商户属性为“MP01|MP03”时,商户经营地址必填", context);
            return false;
        }

        if (dto.getCdtrInf() != null
                && (MP02.equals(dto.getCdtrInf().getMrchntPrprty()) || MP03.equals(dto.getCdtrInf().getMrchntPrprty()))
                && (dto.getMrchntTerInf() == null || StringUtils.isBlank(dto.getMrchntTerInf().getPltfrmNm()))) {
            CheckUtils.changeValidatorMsg("当商户属性为“MP02|MP03”时,网络交易平台名称必填", context);
            return false;
        }
        return true;
    }
}
