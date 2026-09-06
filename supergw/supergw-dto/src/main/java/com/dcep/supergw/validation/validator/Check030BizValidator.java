/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc030.Dcep03000101DTO;
import com.dcep.supergw.validation.Check030Biz;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 030报文校验器
 *
 * @author qinchaoyong
 * @date 2024/12/12 12:38
 */
public class Check030BizValidator implements ConstraintValidator<Check030Biz, Object> {
    /**
     * PR00
     */
    private static final String RSPN_STS_PR00 = "PR00";

    @Override
    public void initialize(Check030Biz constraint) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep03000101DTO)) {
            return true;
        }
        Dcep03000101DTO dto = (Dcep03000101DTO) value;

        // 开始复合字段的逻辑判断
        if (null != dto.getRspnInf() && null != dto.getRspnInf().getRspnSts()) {
            if (RSPN_STS_PR00.equals(dto.getRspnInf().getRspnSts())) {
                if (null == dto.getRspnPgInf()) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，应答分页信息必填", context);
                    return false;
                }

                if (null == dto.getPdctModfyAudtRcrdList()
                        || dto.getPdctModfyAudtRcrdList().size() < 1) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，产品修改审核记录列表必填", context);
                    return false;
                }
            }
        }

        return true;
    }
}