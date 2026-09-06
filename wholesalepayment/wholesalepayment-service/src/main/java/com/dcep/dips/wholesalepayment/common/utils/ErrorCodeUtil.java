/*
 * pbcdci.cn Inc.
 * Copyright © 2022 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.common.utils;

import com.dcep.dips.wholesalepayment.config.ErrorCodeProperties;
import com.dcep.dips.wholesalepayment.config.ErrorCodeProperties.DcepErrInfo;
import com.dcep.dips.wholesalepayment.config.ErrorCodeProperties.McbsErrInfo;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 错误码转换工具类，需在Spring容器环境下执行。
 *
 * @author wxy
 * @version $Id: ErrorCodeUtil.java, v 0.1 2022年6月6日 下午7:59:39 wxy Exp $
 */
@Slf4j
public final class ErrorCodeUtil {

    private ErrorCodeUtil() {
        // comply with Sonar rules.
    }

    /**
     * 查询货币桥错误信息串对应的DCEP错误码
     *
     * @return DCEP错误码
     */
    public static String getDcepErrorCode(String mBridgeAddtlInf) {
        if (mBridgeAddtlInf == null) {
            return mBridgeCode2DcepCode(null);
        }
        int idx = mBridgeAddtlInf.indexOf(':');
        String mBridgeCode;
        if (idx == -1) {
            mBridgeCode = mBridgeAddtlInf;
        } else {
            mBridgeCode = mBridgeAddtlInf.substring(0, idx);
        }
        return mBridgeCode2DcepCode(mBridgeCode);
    }

    /**
     * 查询DCEP错误码对应的货币桥错误信息串
     *
     * @param dcepCode DCEP错误码
     * @return 货币桥格式的错误信息串
     */
    public static String getMbridgeStsRsnInfAddtlInf(String dcepCode) {
        McbsErrInfo mBridgeErrInfo = getMbridgeErrInfo(dcepCode);
        if (mBridgeErrInfo == null) {
            log.info("getMbridgeStsRsnInfAddtlInf {} not existed", dcepCode);

            // 取货币桥默认错误码 + DCEP错误码作为货币桥错误信息
            return Constant.MBRIDGE_DEFAULT_ERRCODE + ":" + "DCEP-" + dcepCode;
        }
        return mBridgeErrInfo.getMcbsCode() + ":" + mBridgeErrInfo.getMcbsMsg();
    }

    /**
     * 货币桥错误码--》dcep错误码
     *
     * @param mBridgeCode 货币桥错误码
     * @return dcep错误码
     */
    public static String mBridgeCode2DcepCode(String mBridgeCode) {
        DcepErrInfo dcepErrInfo = getDcepErrInfo(mBridgeCode);
        if (dcepErrInfo == null) {
            log.info("mBridgeCode2DcepCode {} not existed", mBridgeCode);
            return Constant.DCEP_DEFAULT_ERRCODE;
        }
        return dcepErrInfo.getDcepCode();
    }

    /**
     * 货币桥错误码--》dcep错误信息
     *
     * @param mBridgeCode 货币桥错误码
     * @return dcep错误信息
     */
    public static String mBridgeCode2DcepMsg(String mBridgeCode) {
        DcepErrInfo dcepErrInfo = getDcepErrInfo(mBridgeCode);
        if (dcepErrInfo == null) {
            log.info("mBridgeCode2DcepMsg {} not existed", mBridgeCode);
            return Constant.DCEP_DEFAULT_ERRMSG;
        }
        return dcepErrInfo.getDcepMsg();
    }

    /**
     * dcep错误码--》货币桥错误码
     *
     * @param dcepCode dcep错误码
     * @return 货币桥错误码
     */
    public static String dcepCode2MbridgeCode(String dcepCode) {
        McbsErrInfo mBridgeErrInfo = getMbridgeErrInfo(dcepCode);
        if (mBridgeErrInfo == null) {
            log.info("dcepCode2MbridgeCode {} not existed", dcepCode);
            return Constant.MBRIDGE_DEFAULT_ERRCODE;
        }
        return mBridgeErrInfo.getMcbsCode();
    }

    /**
     * dcep错误码--》货币桥错误信息
     *
     * @param dcepCode dcep错误码
     * @return 货币桥错误信息
     */
    public static String dcepCode2MbridgeMsg(String dcepCode) {
        McbsErrInfo mBridgeErrInfo = getMbridgeErrInfo(dcepCode);
        if (mBridgeErrInfo == null) {
            log.info("dcepCode2MbridgeMsg {} not existed", dcepCode);
            return Constant.MBRIDGE_DEFAULT_ERRMSG;
        }
        return mBridgeErrInfo.getMcbsMsg();
    }

    /**
     * 货币桥错误码--》dcep错误信息对象
     *
     * @param mBridgeCode 货币桥错误码
     * @return dcep错误信息对象
     */
    public static DcepErrInfo getDcepErrInfo(String mBridgeCode) {
        DcepErrInfo dcepErrInfo = null;
        Map<String, DcepErrInfo> mBridgeErrMap = ErrorCodeProperties.getMcbs();
        if (mBridgeErrMap == null) {
            log.info("getDcepErrInfo: mBridge errorcode map not existed");
        } else {
            dcepErrInfo = mBridgeErrMap.get(mBridgeCode);
        }
        return dcepErrInfo;
    }

    /**
     * dcep错误码--》货币桥错误信息对象
     *
     * @param dcepCode dcep错误码
     * @return 货币桥错误信息对象
     */
    public static McbsErrInfo getMbridgeErrInfo(String dcepCode) {
        McbsErrInfo mBridgeErrInfo = null;
        Map<String, McbsErrInfo> dcepMap = ErrorCodeProperties.getDcep();
        if (dcepMap == null) {
            log.info("getMbridgeErrInfo: dcep errorcode map not existed");
        } else {
            mBridgeErrInfo = dcepMap.get(dcepCode);
        }
        return mBridgeErrInfo;
    }

}
