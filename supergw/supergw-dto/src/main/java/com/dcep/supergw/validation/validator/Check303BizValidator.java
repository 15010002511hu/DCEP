package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc301.Dcep30100101DTO;
import com.dcep.supergw.validation.Check303Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class Check303BizValidator implements ConstraintValidator<Check303Biz, Object> {

    private static final String MP01 = "MP01";
    private static final String MP02 = "MP02";
    private static final String MP03 = "MP03";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep30100101DTO)) {
            return true;
        }
        Dcep30100101DTO dto = (Dcep30100101DTO) value;

        if (dto.getCdtrInf() != null
                && (MP01.equals(dto.getCdtrInf().getMrchntPrprty()) || MP03.equals(dto.getCdtrInf().getMrchntPrprty()))
                && (dto.getMrchntTerInf() == null || StringUtils.isBlank(dto.getMrchntTerInf().getMrchntBizAddr()))) {
            CheckUtils.changeValidatorMsg("当商户属性为“MP01|MP03”时,商户经营地址必填", context);
            return false;
        }

        if (dto.getCdtrInf() != null
                && (MP02.equals(dto.getCdtrInf().getMrchntPrprty()) || MP03.equals(dto.getCdtrInf().getMrchntPrprty()))
                && (dto.getOrdrInf() == null || StringUtils.isBlank(dto.getOrdrInf().getPltfrmNm()))) {
            CheckUtils.changeValidatorMsg("当商户属性为“MP02|MP03”时,网络交易平台名称必填", context);
            return false;
        }

        return true;
    }
}

