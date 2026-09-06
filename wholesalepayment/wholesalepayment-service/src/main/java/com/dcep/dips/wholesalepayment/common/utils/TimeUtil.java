/*
 * pbcdci.cn Inc.
 * Copyright © 2022 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.common.utils;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.utils.DcepDateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 货币桥使用的0时区时间与互联互通使用的8时区时间的转换工具类
 * @author laowei
 * @version $Id: TransferTime.java, v 0.1 2022年5月17日 下午7:03:08 laowei Exp $
 */
public final class TimeUtil {
    
    /** 一小时毫秒数 */
    public static final long ONE_HOUR_MS = 60 * 60 * 1000L;

    /** 8时区和0时区时差（ms） */
    public static final long MILLIS_DIFF = ONE_HOUR_MS * 8L;

    /** 一天毫秒数*/
    public static final long DAY_HOUR_MS = ONE_HOUR_MS * 24;

    /** dcep日期格式*/
    public static final String DCEP_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private TimeUtil() {
        // comply with Sonar rules.
    }
    
    /**
     * 转成货币桥使用的0时区时间串
     * @param hlhtTime 互联互通8时区时间串
     * @param timePattern 时间模式串
     * @return 0时区时间串
     */
    public static String toMcbs(String hlhtTime, String timePattern) {
        try {
            long msTime = DcepDateUtils.parseDate(hlhtTime, timePattern).getTime();
            msTime -= MILLIS_DIFF;
            return DcepDateUtils.formateDate(new Date(msTime), timePattern);
        } catch (ParseException e) {
            throw new DcepException(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(),
                    "8时区时间(" + hlhtTime + ")转0时区格式(" + timePattern + ")失败", e);
        }
    }

    /**
     * 转成互联互通使用的8时区时间串
     * @param mcbsTime 货币桥0时区时间串
     * @param timePattern 时间模式串
     * @return 8时区时间串
     */
    public static String toHlht(String mcbsTime, String timePattern) {
        try {
            long msTime = DcepDateUtils.parseDate(mcbsTime, timePattern).getTime();
            msTime += MILLIS_DIFF;
            return DcepDateUtils.formateDate(new Date(msTime), timePattern);
        } catch (ParseException e) {
            throw new DcepException(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(),
                    "0时区时间(" + mcbsTime + ")转8时区格式(" + timePattern + ")失败", e);
        }
    }
    
    /**
     * 获取货币桥的当前时间串（0时区）
     * @param timePattern 时间模式串
     * @return 0时区当前时间串
     */
    public static String getMcbsCurrentTime(String timePattern) {
        long msTime = System.currentTimeMillis() - MILLIS_DIFF;
        return DcepDateUtils.formateDate(new Date(msTime), timePattern);
    }
    
    /**
     * 获取互联互通的当前时间串（8时区）
     * @param timePattern 时间模式串
     * @return 8时区当前时间串
     */
    public static String getHlhtCurrentTime(String timePattern) {
        return DcepDateUtils.formateDate(new Date(), timePattern);
    }

    /**
     * 获取互联互通的上一小时时间串（8时区）
     * @param timePattern 时间模式串
     * @return 8时区上一小时时间串
     */
    public static String getHlhtLastHourTime(String timePattern) {
        long msTime = System.currentTimeMillis() - ONE_HOUR_MS;
        return DcepDateUtils.formateDate(new Date(msTime), timePattern);
    }

    /**
     * 获取互联互通的上一小时时间（8时区）
     * @return 8时区上一小时时间
     */
    public static Date getHlhtLastHourDate(){
        return new Date( System.currentTimeMillis() - ONE_HOUR_MS);
    }

    /**
     * MCBS货币桥时间串转成互联互通使用的8时区时间串
     * @param mBridgeTime MCBS货币桥时间串
     * @param timePattern 时间模式串
     * @return 8时区时间串
     */
    public static String fromMcbs(String mBridgeTime, String timePattern) {
        try {
            long msTime = DcepDateUtils.parseDate(mBridgeTime, timePattern).getTime();
            msTime += MILLIS_DIFF;
            return DcepDateUtils.formateDate(new Date(msTime), timePattern);
        } catch (ParseException e) {
            throw new DcepException(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(),
                    "MCBS货币桥时间(" + mBridgeTime + ")转8时区格式(" + timePattern + ")失败", e);
        }
    }

    /**
     * 将Date类型转换为String类型
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DCEP_TIME_PATTERN);
        return sdf.format(date);
    }
    
}
