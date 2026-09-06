/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc006.Dcep00600101DTO;
import com.dcep.supergw.validation.Check006Biz;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 006报文校验器
 *
 * @author qinchaoyong
 * @date 2024/12/12 12:38
 */
public class Check006BizValidator implements ConstraintValidator<Check006Biz, Object> {
    /**
     * PR00
     */
    private static final String RSPN_STS_PR00 = "PR00";

    @Override
    public void initialize(Check006Biz constraint) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep00600101DTO)) {
            return true;
        }
        Dcep00600101DTO dto = (Dcep00600101DTO) value;

        // 开始复合字段的逻辑判断
        if (null != dto.getRspnInf() && null != dto.getRspnInf().getRspnSts()) {
            if (RSPN_STS_PR00.equals(dto.getRspnInf().getRspnSts())) {
                if (null == dto.getRspnPgInf()) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，应答分页信息必填", context);
                    return false;
                }

                if (CollectionUtils.isEmpty(dto.getPdctList())) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，合约产品列表必填", context);
                    return false;
                }
            }
        }

        return true;
    }
}