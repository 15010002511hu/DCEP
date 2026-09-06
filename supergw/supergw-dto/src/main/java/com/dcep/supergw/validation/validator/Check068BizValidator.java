/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc068.Dcep06800101DTO;
import com.dcep.supergw.dto.dc068.SubDist;
import com.dcep.supergw.validation.Check068Biz;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * 006报文校验器
 *
 * @author qinchaoyong
 * @date 2024/12/12 12:38
 */
public class Check068BizValidator implements ConstraintValidator<Check068Biz, Object> {
    /**
     * PR00
     */
    private static final String RSPN_STS_PR00 = "PR00";

    /**
     * PR01
     */
    private static final String RSPN_STS_PR01 = "PR01";

    @Override
    public void initialize(Check068Biz constraint) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep06800101DTO)) {
            return true;
        }
        Dcep06800101DTO dto = (Dcep06800101DTO) value;

        // 开始复合字段的逻辑判断
        if (null != dto.getRspnInf() && null != dto.getRspnInf().getRspnSts()) {
            if (RSPN_STS_PR00.equals(dto.getRspnInf().getRspnSts())) {
                if (null == dto.getSubDistList()) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，补贴发放列表必填", context);
                    return false;
                }

                if (CollectionUtils.isEmpty(dto.getSubDistList())) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，补贴发放列表必填", context);
                    return false;
                }

                List<SubDist> subDist = dto.getSubDistList();
                for (int i = 0; i < subDist.size(); i++) {
                    SubDist subDist1 = subDist.get(i);
                    if (StringUtils.isEmpty(subDist1.getWltId())) {
                        CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，补贴发放钱包ID必填", context);
                        return false;
                    }
                }
            }

            if (RSPN_STS_PR01.equals(dto.getRspnInf().getRspnSts())) {
                if (StringUtils.isEmpty(dto.getRspnInf().getRjctCd())) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR01时，业务拒绝码必填", context);
                    return false;
                }
            }
        }

        return true;
    }
}