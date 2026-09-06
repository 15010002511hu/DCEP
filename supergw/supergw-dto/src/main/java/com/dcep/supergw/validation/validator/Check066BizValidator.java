/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc066.Dcep06600101DTO;
import com.dcep.supergw.dto.dc066.SubPreDist;
import com.dcep.supergw.validation.Check066Biz;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * 066报文校验器
 *
 * @author qinchaoyong
 * @date 2024/12/12 12:38
 */
public class Check066BizValidator implements ConstraintValidator<Check066Biz, Object> {

    /**
     * PR00
     */
    private static final String RSPN_STS_PR00 = "PR00";

    /**
     * PR01
     */
    private static final String RSPN_STS_PR01 = "PR01";

    @Override
    public void initialize(Check066Biz constraint) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep06600101DTO)) {
            return true;
        }
        Dcep06600101DTO dto = (Dcep06600101DTO) value;

        // 开始复合字段的逻辑判断
        if (null != dto.getRspnInf() && null != dto.getRspnInf().getRspnSts()) {
            if (RSPN_STS_PR00.equals(dto.getRspnInf().getRspnSts())) {
                if (null == dto.getSubPreDistList()) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，补贴预发放列表必填", context);
                    return false;
                }

                if (CollectionUtils.isEmpty(dto.getSubPreDistList())) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR00时，补贴预发放列表必填", context);
                    return false;
                }

            }

            if (RSPN_STS_PR01.equals(dto.getRspnInf().getRspnSts())) {
                if (StringUtils.isEmpty(dto.getRspnInf().getRjctCd())) {
                    CheckUtils.changeValidatorMsg("当业务回执状态为PR01时，业务拒绝码必填", context);
                    return false;
                }
            }

            List<SubPreDist> subPreDistList = dto.getSubPreDistList();
            if (null != subPreDistList && !subPreDistList.isEmpty()) {
                for (int i = 0; i < subPreDistList.size(); i++) {
                    SubPreDist subPreDist = subPreDistList.get(i);
                    if (RSPN_STS_PR01.equals(subPreDist.getPreDistProcRlt())) {
                        if (StringUtils.isEmpty(subPreDist.getPreDistProcDesc())) {
                            CheckUtils.changeValidatorMsg(
                                "当subPreDistList[" + i + "]业务回执状态为PR01时，预发放处理说明必填", context);
                            return false;
                        }
                    }
                }
            }
            if (null != subPreDistList && !subPreDistList.isEmpty()) {
                for (int i = 0; i < subPreDistList.size(); i++) {
                    SubPreDist subPreDist1 = subPreDistList.get(i);
                    if (RSPN_STS_PR00.equals(subPreDist1.getPreDistProcRlt()) && StringUtils.isEmpty(
                        subPreDist1.getWltId())) {
                        CheckUtils.changeValidatorMsg(
                            "当subPreDistList[\" + i + \"]业务回执状态为PR00时，补贴预发放钱包ID必填", context);
                        return false;
                    }
                }
            }
        }

        return true;
    }
}