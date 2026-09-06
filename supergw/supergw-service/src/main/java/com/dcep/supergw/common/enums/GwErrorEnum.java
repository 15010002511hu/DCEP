/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.common.enums;

/**
 * @author huyajun
 * @version $Id: GwServiceErrorEnum.java, v 0.1 2019年1月16日 下午3:09:50 Administrator Exp $
 */
public enum GwErrorEnum {

    /**
     * 未知异常
     */
    UNKNOWN_EXCEPTION("DCEPS9999", "其他系统错"),

    /**
     * 请求参数非法
     */
    REQUEST_PARAM_ILLEGAL("DCEPS2021", "请求参数非法"),

    /**
     * 返回参数非法
     */
    RESP_PARAM_ILLEGAL("DCEPS2022", "返回参数非法"),

    /**
     * 数据库操作失败
     */
    MANAGER_DAL_ERROR("DCEPS5999", "其他数据库错"),

    /**
     * manager层失败
     */
    MANAGER_MAN_ERROR("DCEPS9007", "系统调用失败"),

    /**
     * Dubbo服务调用失败
     */

    MANAGER_RPC_ERROR("DCEPS9011", "Dubbo服务调用失败"),

    /**
     * 业务处理超时，用于http请求对应的超时
     */
    MANAGER_MAN_TIMEOUT("DCEPS9004", "业务处理超时"),

    /**
     * XML转DTO失败
     */
    XML2DTO_ERROR("DCEPS2023", "XML转DTO失败"),

    /**
     * DTO转XML失败
     */
    DTO2XML_ERROR("DCEPS2024", "DTO转XML失败"),

    /**
     * 签名异常
     */
    SIGN_ERROR("DCEPS6005", "数字签名失败"),

    /**
     * 验证签名异常
     */
    VIRIFY_SIGN_ERROR("DCEPS6004", "核验签名失败"),

    /**
     * 加密异常
     */
    ENCRYPT_ERROR("DCEPS6013", "敏感信息加密失败"),

    /**
     * 解密异常decrypt
     */
    DECRYPT_ERROR("DCEPS6012", "敏感信息解密失败"),

    /**
     * 数字信封解密失败
     */
    ENVLP_DECRYPT_ERROR("DCEPS6014", " 数字信封解密失败"),

    /**
     * 数字信封生成失败
     */
    ENVLP_ENCRYPT_ERROR("DCEPS6017", " 数字信封生成失败"),

    /**
     * 参数校验异常,报文校验失败
     */
    VALIDATION_ERROR("DCEPO1012", "参数校验异常"),

    /**
     * 报文类型非法
     */
    MSGTP_ERROR("DCEPS2001", "报文类型非法"),

    /**
     * S1002	读文件失败
     */
    READ_FILE_ERROR("DCEPS1002", "读文件失败"),

    /**
     * O0110	缺少报文域或报文域无数据
     */
    MESSAGE_ERROR("DCEPO0110", "缺少报文域或报文域无数据"),

    LIMITING_ERROR("DCEPS0001", "平台限制交易流量"),
    HTTPS_CERTIFICATE_ERROR("DCEPS6006", "HTTPS证书错误"),
    SYSTEM_PARAMETER_MISSING("DCEPS9001", "系统缺少重要的配置参数"),
    SYSTEM_CONFIG_ERROR("DCEPS9002", "系统配置参数错误"),
    ENVIROMENT_ERROR("DCEPS9003", "系统设备故障或环境异常"),
    SOAPHEADER_ERROR("DCEPO0104", "缺少报文块"),
    SYSTEM_MODULE_ERROR("DCEPS9006", "系统缺少相应的模块"),
    SYSTEM_CALL_PARAMETER_ERROR("DCEPS9008", "系统调用参数错误"),
    SENDER_ERROR("DCEPO3019", "发起运营机构故障"),
    RECEIVER_ERROR("DCEPO3023", "接收运营机构故障"),
    BUSSINESS_ERROR("DCEPO6054", "排队业务处理失败"),

    MSGSN_MSGID_NOT_MATCH_ERROR("DCEPO1034", "通信级标识号与报文标识号不匹配"),
    SENDER_NOT_MATCH_ERROR("DCEPO2020", "报文头发起运营机构非正文发起运营机构"),
    RECEIVER_NOT_MATCH_ERROR("DCEPO2021", "报文头接收运营机构非正文接收运营机构"),
    SOAPHEADER_SOAPBODY_NOT_MATCH_ERROR("DCEPO1036", "报文头与正文信息不匹配"),
    SNDDTTM_ERROR("DCEPO1003", "报文日期早于可受理业务日期范围最早日期"),
    SNDDTTM_ERROR1("DCEPO1017", "报头日期非法"),
    MSGTP_NOT_IN_MSGID_ERROR("DCEPO1037", "报文标识号中的报文编号非法"),

    /**
     * 获取应答报文数据失败
     */
    READ_MESSAGE_ERROR("DCEPS2013", "获取应答报文数据失败"),
    /**
     * 发送放机构无权限发送报文
     */
    SENDER_NON_PRIVILEGED("DCEPO5001", "无权发起本类报文"),
    /**
     * 接收方机构无权限接收该报文
     */
    RECEIVER_NON_PRIVILEGED("DCEPO5002", "无权接收本类报文"),
    NULL_ERROR("DCEPS2400", "必填字段未填写"),

    /*目的地无法到达*/
    FOWARD_NGINX_EROR("DCEPS3005", "目的地无法到达"),
    /* 获取ZONE信息失败 */
    ZONE_INFO_ERROR("DCEPS3004", "获取ZONE信息失败"),

    PERF_TEST_SWITCH_CLOSE_ERROR("DCEPS9013", "压测开关未打开"),

    /**
     * 无权发起或接收此类业务
     */
    SENDER_NON_SUCH_BIZ_PRIVILEGED("DCEPO5006", "无权发起或接收此类业务"),

    /**
     * 无权发起或接收此类业务
     */
    RECEIVER_NON_SUCH_BIZ_PRIVILEGED("DCEPO5006", "无权发起或接收此类业务"),

    /**
     * 报文类型与业务类型不匹配
     */
    MSGTYPE_UNMATCH_BIZTYPE("DCEPO1104", "报文类型与业务类型不匹配"),

    /**
     * 报文类型与业务类型已过失效时间
     */
    MSGTYPE_BIZTYPE_TIME_INVALID("DCEPO1104", "报文类型与业务类型已过失效时间"),

    /**
     * 业务种类与业务类型不匹配
     */
    BIZCTGY_UNMATCH_BIZTYPE("DCEPO1105", "业务种类与业务类型不匹配"),

    /**
     * 业务种类与业务类型已过失效时间
     */
    BIZCTGY_BIZTYPE_TIME_INVALID("DCEPO1105", "业务种类与业务类型已过失效时间"),

    LOADTRAFFIC_MSGID_NOT_MATCH_ERROR("DCEPS9014", "压测流量标识LoadTraffic与msgId环境标识位不匹配");



    /**
     * 枚举编码
     */
    private final String code;

    /**
     * 描述说明
     */
    private final String description;

    /**
     * 私有构造函数。
     *
     * @param code        枚举编码
     * @param description 描述说明
     */
    private GwErrorEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    public static GwErrorEnum getEnum(String code) {
        for (GwErrorEnum en : GwErrorEnum.values()) {
            if (en.getCode().equals(code)) {
                return en;
            }
        }
        return GwErrorEnum.UNKNOWN_EXCEPTION;
    }

}
