/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.dips.wholesalepayment.enums.BizPrtyEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ClearingDTO extends RecordDTO{

    /**
     * 交易批次号
     */
    @JsonIgnore
    void clrBatId(String clrBatId);
    
    String clrBatId();

    String clrTransTp();

    /**
     * 报文编号
     */
    @JsonIgnore
    String clrMsgTp();

    /**
     * 端到端流水号
     */
    @JsonIgnore
    String clrEndToEndId();

    /**
     * 报文标识号
     */
    @JsonIgnore
    String getClrMsgId();
    /**
     * 业务类型编码
     */
    @JsonIgnore
    String clrBizTp();

    /**
     * 业务种类编码
     */
    @JsonIgnore
    String clrBizKind();

    /**
     * 付款运营机构
     */
    @JsonIgnore
    String clrDbtrPtyId();

    /**
     * 付款ID
     */
    @JsonIgnore
    String clrDbtrWltId();

    /**
     * 付款系统标识
     */
    @JsonIgnore
    String clrDbtrSysId();

    /**
     * 收款运营机构
     */
    @JsonIgnore
    String clrCdtrPtyId();

    /**
     * 收款ID
     */
    @JsonIgnore
    String clrCdtrWltId();

    /**
     * 收款系统标识
     */
    @JsonIgnore
    String clrCdtrSysId();

    /**
     * Database Column Remarks: 交易币种
     */
    @JsonIgnore
    String clrCurrency();

    /**
     * 交易金额
     */
    @JsonIgnore
    String clrAmt();

    /**
     * 业务回执状态
     */
    @JsonIgnore
    String clrBizRspSts();

    /**
     * 业务回执状态，赋值方法
     */
    void clrBizRspSts(String clrBizRspSts);

    /**
     * 业务拒绝码
     */
    @JsonIgnore
    String clrBizRjctCd();

    /**
     * 业务拒绝码，赋值方法
     * @param clrBizRjctCd
     */
    void clrBizRjctCd(String clrBizRjctCd);

    /**
     * 业务拒绝原因
     */
    @JsonIgnore
    String clrRjctResn();

    /**
     * 业务拒绝原因，赋值方法
     * @param clrRjctResn
     */
    void clrRjctResn(String clrRjctResn);

    /**
     * 交易描述信息
     */
    @JsonIgnore
    String clrTrxInf();

    /**
     *   对账标识 "0:不对账 1:对账"
     */
    @JsonIgnore
    String clrFlag();

    /**
     * 账务时间
     */
    String clrCreDtTm();
    /**
     * 发送方机构
     */
    @JsonIgnore
    String clrSendPtyId();

    /**
     * 发送系统标识
     */
    @JsonIgnore
    String clrSendSysId();

    /**
     * 接收方机构
     */
    @JsonIgnore
    String clrRecvPtyId();

    /**
     * 接收系统标识
     */
    @JsonIgnore
    String clrRecvSysId();

    /**
     * 超时推定时间
     */
    String clrPresumeTm();
    /**
     * 赋值batchID
     *
     * @param response
     */
    void fillBatchId(Response<ClearingStatus> response);

    /**
     * 赋值业务状态(平台)
     */
    default void fillPlatPrcSts(String bizSts) {};

    default OrgnlGrpHdr presumeConfirm() {
        return null;
    }
    
    /**
     * 记账类型
     */
    String clrAcctTp();
    
    /**
     * 发起渠道
     */
    default String clrSndChnlSys() {
        return null;
    }
    
    /**
     * 接收渠道
     */
    default String clrRcvChnlSys() {
        return null;
    }
    
    /**
     * 货币桥信息
     */
    default MbridgeReqDTO clrMbridgeInf() {
        return null;
    }
    
    /**
     * 填充报文标识号
     */
    default void fillMsgId(String msgId, String orgMsgId) {}
    
    /**
     * 填充差错报文信息
     */
    default void fillDsptInf(String msgId, String orgMsgId, String orgMsgTp, String orgInstgPty,
            ActiveCurrencyAndAmount orgTxAmt) {
    }
    
    /**
     * 填充报文体接收机构
     */
    default void fillInstdDrctPty(String instdDrctPty) {}

    default ContractInfo clrContractInfo(){
        return null;
    }

    /**
     * 业务优先级
     */
    default String clrBizPrty(){
        return BizPrtyEnum.NORM.getCode();
    }

    /**
     * 系统工作日期
     */
    default String clrSysWorkDt(){
        return null;
    }

    /**
     * 填充结算日期
     */
    default void fillSttlmDt(String sttlmDt) {}
}