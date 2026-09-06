/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc026.Dcep02600101DTO;
import com.dcep.supergw.validation.Check026Biz;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 026报文校验器
 *
 * @author qinchaoyong
 * @date 2024/12/12 12:38
 */
public class Check026BizValidator implements ConstraintValidator<Check026Biz, Object> {
    /**
     * PR00
     */
    private static final String RSPN_STS_PR00 = "PR00";

    @Override
    public void initialize(Check026Biz constraint) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep02600101DTO)) {
            return true;
        }
        Dcep02600101DTO dto = (Dcep02600101DTO) value;

        // 开始复合字段的逻辑判断
        if (null != dto.getRspnInf() && null != dto.getRspnInf().getRspnSts()) {
            if (RSPN_STS_PR00.equals(dto.getRspnInf().getRspnSts())) {
                if (null == dto.getRspnPgInf()) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，合约产品创建审核记录列表查询信息必填", context);
                    return false;
                }

                if (CollectionUtils.isEmpty(dto.getPdctCretAudtRcrdList())) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，合约产品创建审核记录列表查询信息必填", context);
                    return false;
                }
            }
        }

        return true;
    }
}