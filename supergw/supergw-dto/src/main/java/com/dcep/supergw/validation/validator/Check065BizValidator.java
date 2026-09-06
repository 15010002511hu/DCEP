/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc065.Dcep06500101DTO;
import com.dcep.supergw.dto.dc065.SubPreDist;
import com.dcep.supergw.validation.Check065Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import jodd.util.StringUtil;

/**
 * 066报文校验器
 *
 * @author qinchaoyong
 * @date 2024/12/12 12:38
 */
public class Check065BizValidator implements ConstraintValidator<Check065Biz, Object> {

    /**
     * PR00
     */
    private static final String RPT01 = "RPT01";

    @Override
    public void initialize(Check065Biz constraint) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep06500101DTO)) {
            return true;
        }
        Dcep06500101DTO dto = (Dcep06500101DTO) value;

        // 开始复合字段的逻辑判断
        if (!dto.getSubPreDistList().isEmpty()) {
            for(SubPreDist subPreDist : dto.getSubPreDistList()){
                if(RPT01.equals(subPreDist.getSubTp())){
                    if(StringUtil.isBlank(subPreDist.getUseLimit())){
                        CheckUtils.changeValidatorMsg("当补贴类型SubTp为PRT01时，补贴使用限制UseLimit必填", context);
                        return false;
                    }
                }
            }
        }

        return true;
    }
}