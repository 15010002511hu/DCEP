/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.enums;

/**
 * 
 * @author liuzhenli
 * @version $Id: MsgTpEnum.java, v 0.1 2019年8月28日 上午10:47:10 liuzhenli Exp $
 */
public enum MsgTpEnum {

    FI_CDT("dcep.112.001.01", "金融机构汇款报文"),
    FI_RETUNE("dcep.114.001.01", "金融机构退汇报文"),
    CCP_REQUEST("dcep.131.001.01", "CCP业务发起报文"),
    FMI_SSS_REQUEST("dcep.135.001.01", "FMI发起SSS业务申请报文"),
    MER_SSS_REQUEST("dcep.137.001.01", "机构发起SSS业务申请报文"),
    SETTLE_NOTICE("dcep.200.001.01", "结算状态通知报文"),
    CDT_REQUEST("dcep.201.010.01", "付款业务请求报文"),
    CDT_RESPONSE("dcep.202.010.01", "付款业务应答报文"),
    DBT_REQUEST("dcep.211.010.01", "收款业务请求报文"),
    DBT_RESPONSE("dcep.212.010.01", "收款业务应答报文"),
    RECOV_REQUEST("dcep.221.010.01", "兑回业务请求报文"),
    RECOV_RESPONSE("dcep.222.010.01", "兑回业务应答报文"),
    COV_REQUREST("dcep.225.010.01","兑出业务请求报文"),
    COV_RESPONSE("dcep.226.010.01","兑出业务应答报文"),
    CDT_COV_REQUREST("dcep.227.010.01","汇款兑出请求报文"),
    CDT_COV_RESPONSE("dcep.228.010.01","汇款兑出应答报文"),
    REFUND_REQUREST("dcep.281.010.01","消费退款业务请求报文"),
    REFUND_RESPONSE("dcep.282.010.01","消费退款业务应答报文"),
    CRDT_ADJ_REQUREST("dcep.801.010.01","差错贷记调账请求报文"),
    CRDT_ADJ_RESPONSE("dcep.802.010.01","差错贷记调账应答报文"),
    ORDR_CONF_REQUEST("dcep.261.010.01", "商户订单确认申请报文"),
    ORDR_CONF_RESPONSE("dcep.262.010.01", "商户订单确认回执报文"),
    ORDR_CONF_RESULT_NOTICE("dcep.263.010.01", "消费付款结果通知报文"),
    CDT_REQUEST_ASYN("dcep.203.010.01", "付款业务请求报文（货币桥）"),
    CDT_REQUEST_NMTBN("dcep.203.010.02", "付款业务请求报文（区块链）"),
    DBT_REQUEST_ASYN("dcep.213.010.01", "收款业务请求报文（货币桥）"),
    DBT_REQUEST_NMTBN("dcep.213.010.02", "收款业务请求报文（区块链）"),
    CDT_FUND_DECREASE("dcep.181.001.01", "资金调减申请报文"),
    CDT_PRE_FUND_DECREASE("dcep.183.001.01", "预注资调减申请报文"),
    FUND_ADJUST_NOTICE("dcep.185.001.01", "资金调整通知报文"),
    CDT_FUND_INCREASE("hvps.112.001.01", "大额汇兑来账请求"),
    CDT_FUND_PRE_INCREASE("hvps.115.001.01", "大额延迟结算来账请求"),

    REVERSE_REQUREST("dcep.427.001.01", "业务撤销请求报文"),
    REVERSE_RESPONSE("dcep.428.001.01", "业务撤销通知报文"),

    CDT_FUND_DECREASE_OUT("hvps.112.001.01", "注资调减"),
    CDT_PRE_FUND_DECREASE_OUT("hvps.118.001.01", "预注资调减"),
    CDT_FUND_INCREASE_IN("hvps.112.001.01", "注资调增"),
    CDT_PRE_FUND_INCREASE_IN("hvps.115.001.01", "预注资调增"),

    COMMON_PROCESS_CONFIRM("dcep.900.001.01", "通用处理确认报文"),
    MESSAGE_DISCARD_NOTICE("dcep.911.001.01", "报文丢弃通知报文"),
    COMMON_PROCESS_COMCONF("dcep.902.001.01", "通信级确认报文"),
    FINALNOTICE("dcep.909.001.01", "交易明细查询应答报文"),

    TXN_STATE_REQUEST("dcep.411.001.01", "交易状态查询请求报文"),
    TXN_STATE_RESPONSE("dcep.412.001.01", "交易状态查询应答报文"),

    SETTLE_NOTICE_ABBR("200", "结算状态通知报文"),
    CDT_REQUEST_ABBR("201", "付款业务请求报文"),
    CDT_RESPONSE_ABBR("202", "付款业务应答报文"),
    CDT_COV_REQUREST_ABBR("227","汇款兑出请求报文"),
    CDT_FUND_DECREASE_ABBR("181", "资金调减申请报文"),
    CDT_PRE_FUND_DECREASE_ABBR("183", "预注资调减申请报文"),
    FUND_ADJUST_NOTICE_ABBR("185", "资金调整通知报文"),
    TXN_STATE_REQUEST_ABBR("411", "交易状态查询请求报文"),
    TXN_STATE_RESPONSE_ABBR("412", "交易状态查询应答报文"),
    TXN_DETAIL_RESPONSE_ABBR("418", "交易明细查询应答报文"),
    COMMON_PROCESS_CONFIRM_ABBR("900", "通用处理确认报文"),
    MESSAGE_DISCARD_NOTICE_ABBR("911", "报文丢弃通知报文"),
    COMMON_PROCESS_COMCONF_ABBR("902", "通信级确认报文"),
    FINALNOTICE_ABBR("909", "付款终态通知报文"),

    ZERO_OUT_NOTICE_BCSP("WHOLESALE","区块链清零通知请求"),
    ZERO_OUT_NOTICE_MCBS("mcbs.101.001.01", "货币桥清零通知请求"),
    CDT_REQUEST_MCBS("mcbs.200.001.01", "货币桥贷记报文"),
    DBT_REQUEST_MCBS("mcbs.201.001.01", "货币桥借记报文"),
    PAY_REFUND_MCBS("mcbs.202.001.01", "货币桥支付退回报文"),
    COMMON_PROCESS_CONFIRM_MCBS("mcbs.203.001.01", "货币桥通用处理确认报文"),
    QUERY_STATUS_MCBS("mcbs.204.001.01", "货币桥支付状态查询报文"),

    CHAIN_REQUEST_UP("bcsp.203.010.02", "区块链交易上链"),
    CHAIN_REQUEST_DOWN("bcsp.213.010.02", "区块链交易下链");



    /***
     * 枚举类型
     */
    private final String code;
    /***
     * 描述说明
     */
    private final String description;

    /***
     * 私有构造函数
     * 
     * @param code        错误枚举
     * @param description 描述说明
     */
    private MsgTpEnum(String code, String description) {

        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }
}
