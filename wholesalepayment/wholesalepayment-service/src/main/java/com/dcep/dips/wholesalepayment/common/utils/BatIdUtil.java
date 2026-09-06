/*
 * pbcdci.cn Inc.
 * Copyright © 2022 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.common.utils;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.wholesalepayment.common.constants.Constant;

import java.text.ParseException;
import java.util.Date;

import static com.dcep.dips.wholesalepayment.common.utils.TimeUtil.ONE_HOUR_MS;

/**
 * 批次号获取转换通用类
 * @author lihengzhi
 * @since 2022-05-24
 */

public final class BatIdUtil {

    /** 小时00值 */
    public static final String HOUR_00 = "00";

    /** 批次24顺序号 */
    public static final String BATCH_NO_24 = "24";

    private static final String PARSE_DATE_ERROR = "时间解析异常";


    private BatIdUtil() {
        // comply with Sonar rules.
    }

    /**
     * 通过互联互通时间批次获取mcbs货币桥时间批次
     * @param dcepBatchNo 互联互通时间批次
     * @return mcbs货币桥时间批次
     */
    public static String trsfHlhtBatIdToMcbsBatId(String dcepBatchNo) {
        String mBridgeBatchHour = TimeUtil.toMcbs(
                dcepBatchNo.substring(1, 1 + Constant.BATCH_TIME_PATTERN.length()), Constant.BATCH_TIME_PATTERN);
        if (mBridgeBatchHour.endsWith(HOUR_00)) {
            // 批次修正为mBridgeDate日期-1日的24批次
            Date mBridgeDate;
            try {
                mBridgeDate = DcepDateUtils.parseDate(mBridgeBatchHour, Constant.BATCH_TIME_PATTERN);
            } catch (ParseException e) {
                throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                        PARSE_DATE_ERROR + "(" + mBridgeBatchHour + ")", e);
            }
            mBridgeBatchHour = getLastBatchHourStrOfLastDay(mBridgeDate);
        }
        return dcepBatchNo.substring(0, 1) + mBridgeBatchHour
                + dcepBatchNo.substring(1 + Constant.BATCH_TIME_PATTERN.length());
    }

    /**
     * 通过互联互通时间批次获取mcbs货币桥交易yyyyMMddHH格式的时间字符串
     * @param dcepBatchNo 互联互通时间批次
     * @return mcbs货币桥yyyyMMddHH格式的时间字符串
     */
    public static String trsfHlhtBatIdToMcbsTime(String dcepBatchNo) {
        //step1 获取互联互通批次号对应的yyyyMMddHH格式的交易发生时间
        String hlhtTimeStr = batToTrsTime(dcepBatchNo.substring(1, 1 + Constant.BATCH_TIME_PATTERN.length()));
        //step2 将互联互通时间转为mcbs桥上0时区时间
        return TimeUtil.toMcbs(hlhtTimeStr, Constant.BATCH_TIME_PATTERN);
    }

    /**
     * 通过mcbs货币桥时间批次获取互联互通时间批次
     * @param mbridgeBatchNo mcbs货币桥时间批次
     * @return 互联互通时间批次
     */
    public static String trsfMcbsBatIdToHlhtBatId(String mbridgeBatchNo) {
        String hlhtBatchHour = TimeUtil.fromMcbs(
            mbridgeBatchNo.substring(1, 1 + Constant.BATCH_TIME_PATTERN.length()), Constant.BATCH_TIME_PATTERN);
        if (hlhtBatchHour.endsWith(HOUR_00)) {
            // 批次修正为hlhtDate日期-1日的24批次
            Date hlhtDate;
            try {
                hlhtDate = DcepDateUtils.parseDate(hlhtBatchHour, Constant.BATCH_TIME_PATTERN);
            } catch (ParseException e) {
                throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    PARSE_DATE_ERROR + "(" + hlhtBatchHour + ")", e);
            }
            hlhtBatchHour = getLastBatchHourStrOfLastDay(hlhtDate);
        }
        return mbridgeBatchNo.substring(0, 1) + hlhtBatchHour
                + mbridgeBatchNo.substring(1 + Constant.BATCH_TIME_PATTERN.length());
    }

    /**
     * 获取batchDate日期-1日最后一个批次（24批次）的时间特征串
     * @param batchDate 批次日期
     * @return 批次日期-1日的24批次
     */
    public static String getLastBatchHourStrOfLastDay(Date batchDate) {
        Date lastDay = new Date(batchDate.getTime() - TimeUtil.DAY_HOUR_MS);
        return DcepDateUtils.formateDate(lastDay, Constant.BATCH_DATE_PATTERN) + BATCH_NO_24;
    }


    /**
     * 获取批次号对应的交易发生时间字符串
     * @param batTime 批次号里截取的时间
     * @return 交易发生时间字符串
     */
    public static String batToTrsTime(String batTime) {
        try {
            long msTime = DcepDateUtils.parseDate(batTime, Constant.BATCH_TIME_PATTERN).getTime();
            msTime -= ONE_HOUR_MS;
            return DcepDateUtils.formateDate(new Date(msTime), Constant.BATCH_TIME_PATTERN);
        } catch (ParseException e) {
            throw new DcepException(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(),
                    "批次号(" + batTime + ")转交易发生时间(" + Constant.BATCH_TIME_PATTERN + ")失败", e);
        }
    }

    /**
     * 获取交易发生时间字符串对应的批次号
     * @param trsTime 交易发生时间字符串
     * @return 批次号里的时间
     */
    public static String trsTimeToBat(String trsTime) {
        try {
            long msTime = DcepDateUtils.parseDate(trsTime, Constant.BATCH_TIME_PATTERN).getTime();
            msTime += ONE_HOUR_MS;
            return DcepDateUtils.formateDate(new Date(msTime), Constant.BATCH_TIME_PATTERN);
        } catch (ParseException e) {
            throw new DcepException(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(),
                    "交易时间(" + trsTime + ")转批次号失败", e);
        }
    }

}
