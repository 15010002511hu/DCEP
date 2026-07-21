package com.emop.wlt.user.query.constant;

/**
 * @author hanyabo
 * @version V1.0.0
 * @date 2022/12/27 3:40 PM
 */
public class QRCodeConstant {

    /**
     * QDM01-快捷模式
     */
    public static final String QDM01 = "QDM01";

    /**
     * QDM02-网关模式-查询跳转URL
     */
    public static final String QDM02 = "QDM02";

    /**
     * QDM03-网关模式-访问收款码URL
     */
    public static final String QDM03 = "QDM03";

    /**
     * 银联聚合码
     */
    public static final String QET02 = "QET02";

    /**
     * 数币标准码
     */
    public static final String QET01 = "QET01";

    /**
     * 个人收款码
     */
    public static final String QT01 = "QT01";

    /**
     * 个人付款码
     */
    public static final String QT02 = "QT02";

    /**
     *  商户静态码
     */
    public static final String QT03 = "QT03";

    /**
     * 商户动态码
     */
    public static final String QT04 = "QT04";

    public static final String HTTPS_PROTOCOL = "https";
    public static final String HTTP_PROTOCOL = "http";

    public static final String UWAP_889_CODE = "889";

    /**
     * 数字人民币支付渠道
     */
    public static final String PCT01 = "PCT01";
    public static final String DCEP_APP_NAME = "数字人民币App";

    public static final String DOMAIN_POORT_SPLIT = ":";

    public static final String APP_UNLIMITED_VERSION = "不限制";

    // 版本控制 引导升级枚举
    public static final String QRCODE_DOMS_APP_UPGRADE_KEY = "PR1";

    // 版本控制 可用枚举
    public static final String QRCODE_DOMS_APP_USABLE = "PR2";

    // 版本控制 不可用枚举
    public static final String QRCODE_DOMS_APP_UNUSABLE = "PR3";

    // 版本控制 内测用户组
    public static final String QRCODE_DOMS_TEST_USER_GROUP = "UG1";

    // 版本控制 生产用户组
    public static final String QRCODE_DOMS_ALL_USER_GROUP = "UG2";

}
