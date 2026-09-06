/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.utils;

import com.dcep.common.exception.DcepException;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金额处理工具类

 * @author laowei
 * @version 1.0.0 2025/8/7
 */
@Slf4j
public class AmtUtils {
    public static final String CENTS_PER_YUAN = "100";

    /**
     * 将机构过来的字符串类型的金额，转换为以分单位的金额
     *  1.外部接口DTO中金额统一格式为字符串："12.34"
     *  2.内部DO中的类型为：BigDecimal
     *  3.数据库中为bigint
     *  外部DTO转换为DO：new BigDecimal("12.34").multiply(BigDecimal.valueOf(100))
     *  DO中类型虽然是BigDecimal，但是小数点位数已经为零，可以直接通过mybatis映射为数据库中的bigint
     * @return 不带小数位的BigDecimal类型的金额
     */
    public static BigDecimal toCents(String strYuan) {
        try {
            return new BigDecimal(strYuan).multiply(new BigDecimal(CENTS_PER_YUAN)).setScale(0, RoundingMode.DOWN);
        } catch (Exception e) {
            throw new DcepException(WholesaleErrorEnum.AMOUNT_ERR.getCode(), WholesaleErrorEnum.AMOUNT_ERR.getDescription());
        }
    }

    /**
     * 将元为单元的BigDecimal转换为分为单位的BigDecimal
     * @return 分为单位的BigDecimal
     */
    public static BigDecimal toCents(BigDecimal yuan) {
        try {
            return yuan.multiply(new BigDecimal(CENTS_PER_YUAN)).setScale(0, RoundingMode.DOWN);
        } catch (Exception e) {
            throw new DcepException(WholesaleErrorEnum.AMOUNT_ERR.getCode(), WholesaleErrorEnum.AMOUNT_ERR.getDescription());
        }
    }

    /**
     * 将分为单元的BigDecimal转换为元为单位的BigDecimal
     * @return 元为单位的BigDecimal
     */
    public static BigDecimal toYuan(BigDecimal cents) {
        try {
            return cents.divide(new BigDecimal(CENTS_PER_YUAN), 2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new DcepException(WholesaleErrorEnum.AMOUNT_ERR.getCode(), WholesaleErrorEnum.AMOUNT_ERR.getDescription());
        }
    }
}
