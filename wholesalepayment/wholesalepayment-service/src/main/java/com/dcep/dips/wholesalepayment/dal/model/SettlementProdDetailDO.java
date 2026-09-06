package com.dcep.dips.wholesalepayment.dal.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementProdDetailDO {
    /**
     *   报文标识号 关联结算产品表msgId
     */
    private String msgId;

    /**
     *   明细标识号
     */
    private String txId;

    /**
     *   结算日期
     */
    private String sttlmDt;

    /**
     *   业务交易编号 CCP业务交易编号
     */
    private String instrId;

    /**
     *   业务类型编码
     */
    private String bizTp;

    /**
     *   业务种类编码
     */
    private String bizKind;

    /**
     *   付款机构
     */
    private String dbtrPtyId;

    /**
     *   付款方系统标识
     */
    private String dbtrSysId;

    /**
     *   收款机构
     */
    private String cbtrPtyId;

    /**
     *   收款方系统标识
     */
    private String cbtrSysId;

    /**
     *   交易币种
     */
    private String currency;

    /**
     *   结算金额 单位：分
     */
    private BigDecimal sttlmAmt;

    /**
     *   借贷标识 C：贷记 D：借记
     */
    private String cdtDbtInd;

    /**
     *   业务状态 "PR00: 成功 PR01: 失败 PR02: 处理中 PR10: 清算排队 PR11: 待结算"
     */
    private String bizSts;

    /**
     *   业务处理码
     */
    private String bizPrcCd;

    /**
     *   业务处理信息
     */
    private String bizPrcInf;

    /**
     *   业务处理时间
     */
    private String bizDt;

    /**
     *   分区时间 数据库分区
     */
    private Date partitionTime;

    /**
     *   创建时间
     */
    private Date gmtCreate;

    /**
     *   修改时间
     */
    private Date gmtModified;

}