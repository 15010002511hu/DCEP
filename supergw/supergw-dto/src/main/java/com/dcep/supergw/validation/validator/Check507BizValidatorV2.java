package com.dcep.supergw.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc507.Dcep50700102DTO;
import com.dcep.supergw.validation.Check507BizV2;

public class Check507BizValidatorV2 implements ConstraintValidator<Check507BizV2, Object> {

    /**
     * 当"验证类型"为VT01时,授权码不填写;
     * 当"验证类型"为VT02时,授权码必填;
     * 当"验证类型"为VT03时,授权码必填;
     * 当"验证类型"为VT04时,授权码必填;
     * 当“验证类型”为VT05（正常推送（钱包升级后推送））时，授权码不填写;
     * 当“验证类型”为VT06（拉起推送（钱包升级后推送））时，授权码必填。
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Dcep50700102DTO dto = (Dcep50700102DTO) value;
        String vrfyTp = dto.getPtcInf().getVrfyTp();
        if (("VT01".equals(vrfyTp) || "VT05".equals(vrfyTp))
                && StringUtils.isNotBlank(dto.getPtcInf().getVrfyCd())) {
            CheckUtils.changeValidatorMsg("当验证类型为VT01,VT05时,授权码不填写", context);
            return false;
        }
        if (("VT02".equals(vrfyTp) || "VT03".equals(vrfyTp) || "VT04".equals(vrfyTp)
                || "VT06".equals(vrfyTp)) && StringUtils.isBlank(dto.getPtcInf().getVrfyCd())) {
            CheckUtils.changeValidatorMsg("当验证类型为VT02,VT03,VT04,VT06时,授权码必填", context);
            return false;
        }
        return true;
    }
}
