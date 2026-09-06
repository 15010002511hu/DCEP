/*
 * pbcdci.cn Inc.
 * Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc422.Dcep42200101DTO;
import com.dcep.supergw.validation.Check422Biz;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 422报文校验器
 *
 * @author qinchaoyong
 * @date 2024/12/4 17:24
 */
public class Check422BizValidator implements ConstraintValidator<Check422Biz, Object> {

    /**
     * PR00：原交易处理成功
     */
    private static final String PRTRY_PR00 = "PR00";

    /**
     * PR01：原交易处理失败
     */
    private static final String PRTRY_PR01 = "PR01";

    /**
     * PR02：原交易处理中
     */
    private static final String PRTRY_PR02 = "PR02";

    @Override
    public void initialize(Check422Biz constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep42200101DTO)) {
            return true;
        }

        Dcep42200101DTO dto = (Dcep42200101DTO) value;

        // 查询状态不能既成功又失败
        if (
                (
                        null == dto.getRptOrErr().getBizRpt()
                                && null == dto.getRptOrErr().getOprlErr()
                )
                        ||
                        (
                                null != dto.getRptOrErr().getBizRpt()
                                        && null != dto.getRptOrErr().getOprlErr()
                        )
        ) {
            CheckUtils.changeValidatorMsg("BizRpt与OprlErr不能同时非空，也不能同时为空", context);
            return false;
        }

        // 如果查询成功，再判断原交易状态是否成功
        if (null != dto.getRptOrErr().getBizRpt()) {
            // 查询成功，则原交易处理状态不能既成功又失败
            if ((null == dto.getRptOrErr().getBizRpt().getTxRpt().getTxOrErr().getTx()
                    && null == dto.getRptOrErr().getBizRpt().getTxRpt().getTxOrErr().getBizErr())
                    || (null != dto.getRptOrErr().getBizRpt().getTxRpt().getTxOrErr().getTx()
                    && null != dto.getRptOrErr().getBizRpt().getTxRpt().getTxOrErr().getBizErr())) {
                CheckUtils.changeValidatorMsg("查询成功时，Tx与BizErr不能同时非空，也不能同时为空", context);
                return false;
            }

            // 如果原交易处理成功
            if (PRTRY_PR00.equals(dto.getRptOrErr().getBizRpt().getPmtCmonInf().getCmonSts().getCd().getPrtry())) {
                if (null == dto.getRptOrErr().getBizRpt().getTxRpt().getTxOrErr().getTx()) {
                    CheckUtils.changeValidatorMsg("原交易处理成功时，Tx不能为空", context);
                    return false;
                }

                // 原交易处理成功（PR00），NtryDt必填
                if (null == dto.getRptOrErr().getBizRpt().getPmtCmonInf().getNtryDt()) {
                    CheckUtils.changeValidatorMsg("原交易处理成功时，NtryDt必填", context);
                    return false;
                }
            }

            // 如果原交易处理失败
            if (PRTRY_PR01.equals(dto.getRptOrErr().getBizRpt().getPmtCmonInf().getCmonSts().getCd().getPrtry())) {
                if (null == dto.getRptOrErr().getBizRpt().getTxRpt().getTxOrErr().getBizErr()) {
                    CheckUtils.changeValidatorMsg("原交易处理失败时，BizErr不能为空", context);
                    return false;
                }
            }

        } else {
            // 查询失败的情况
            if (null == dto.getRptOrErr().getOprlErr()) {
                CheckUtils.changeValidatorMsg("查询失败时，OprlErr不能为空", context);
                return false;
            }
        }


        return true;
    }
}
