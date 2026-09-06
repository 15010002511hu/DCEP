package com.dcep.dips.wholesalepayment.dal.model;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.dcep.dips.common.constant.ChnlSysEnum;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.common.utils.PartitionUtil;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustReqDTO;
import com.dcep.dips.wholesalepayment.dto.dc191.CdtTrfTxInf;
import com.dcep.dips.wholesalepayment.dto.dc191.Dcep19100101DTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.AmtUtils;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author wangxiaoyu
 */
@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementProdDO {
    /**
     *   报文标识号
     */
    private String msgId;

    /**
     *   分区时间 数据库分区
     */
    private Date partitionTime;

    /**
     *   报文编号
     * @see MsgTpEnum
     */
    private String msgTp;

    /**
     *   结算日期
     */
    private String sttlmDt;

    /**
     *   批次号
     */
    private String batId;

    /**
     *   业务类型编码
     */
    private String bizTp;

    /**
     *   业务种类编码
     */
    private String bizKind;

    /**
     *   交易类型 "1: 普通转账 2: 三方转账 3: 中央对手方CCP"
     *  @see TransTpEnum
     */
    private String transTp;

    /**
     *   发起机构
     */
    private String sendPtyId;

    /**
     *   发起系统标识
     */
    private String sendSysId;

    /**
     *   接收机构
     */
    private String recvPtyId;

    /**
     *   接收系统标识
     */
    private String recvSysId;

    /**
     *   付款机构
     */
    private String dbtrPtyId;

    /**
     *   付款方系统标识
     */
    private String dbtrSysId;

    /**
     *   付款方ID 付款方钱包/账户ID-密文
     */
    private String dbtrId;

    /**
     *   收款机构
     */
    private String cbtrPtyId;

    /**
     *   收款方系统标识
     */
    private String cbtrSysId;

    /**
     *   收款方ID 收款方钱包/账户ID-密文
     */
    private String cbtrId;

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
     * @see ClearingProdCdtDbtIndEnum
     */
    private String cdtDbtInd;

    /**
     *   业务状态 "PR00: 成功 PR01: 失败 PR02: 处理中 PR10: 清算排队 PR11: 待结算"
     * @see ClearingStatusEnum
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
     *   业务描述信息
     */
    private String bizDescInf;

    /**
     *   对账标识 "0:不对账 1:对账"
     */
    private String clrFlag;

    /**
     *   清算产品 "1:实时 2:不清算"
     */
    private String clrProd;

    /**
     * 区块链数据推送状态 "0:初始化 1:终态 2:推送处理中 3:推送成功"
     */
    private String sendSts;

    /**
     *   创建时间
     */
    private Date gmtCreate;

    /**
     *   修改时间
     */
    private Date gmtModified;



    public void setMsgId(String msgId) {
        this.msgId = msgId == null ? null : msgId.trim();
        this.partitionTime = PartitionUtil.msgCreateTime(this.msgId);
    }

    public SettlementProdDO(String msgId) {
        setGmtModified(new Date());
        setMsgId(msgId);
    }

//    public SettlementProdDO(WholesalePaymentDTO wholesalePaymentDTO) {
//        this.batId = wholesalePaymentDTO.whlPmtBatId();
//        this.msgTp = wholesalePaymentDTO.whlPmtMsgTp();
//
//        setMsgId(wholesalePaymentDTO.whlPmtMsgId());
//
//        this.bizTp = wholesalePaymentDTO.whlPmtBizTp();
//        this.bizKind = wholesalePaymentDTO.whlPmtBizKind();
//        this.transTp = wholesalePaymentDTO.whlPmtTransTp();
//        this.sendPtyId = wholesalePaymentDTO.whlPmtSendPtyId();
//        this.sendSysId = wholesalePaymentDTO.whlPmtSendSysId();
//        this.recvPtyId = wholesalePaymentDTO.whlPmtRecvPtyId();
//        this.recvSysId = wholesalePaymentDTO.whlPmtRecvSysId();
//        this.dbtrPtyId = wholesalePaymentDTO.whlPmtDbtrPtyId();
//        this.dbtrSysId = wholesalePaymentDTO.whlPmtDbtrSysId();
//        this.cbtrPtyId = wholesalePaymentDTO.whlPmtCbtrPtyId();
//        this.cbtrSysId = wholesalePaymentDTO.whlPmtCbtrSysId();
//        this.currency = wholesalePaymentDTO.whlPmtCurrency();
//        this.sttlmAmt = new BigDecimal(wholesalePaymentDTO.whlPmtSttlmAmt());
//        this.cdtDbtInd = wholesalePaymentDTO.whlPmtCdtDbtInd();
//        this.bizSts = ClearingStatusEnum.PROCESS.getCode();
//        this.bizDt = wholesalePaymentDTO.whlPmtBizDt();
//        this.clrFlag = wholesalePaymentDTO.whlPmtClrFlag();
//        this.clrProd = wholesalePaymentDTO.whlPmtClrProd();
//    }

    public SettlementProdDO(ClearingDTO clearingDTO, Clearing clearing) {
        setMsgId(clearingDTO.getClrMsgId());
        this.msgTp = clearingDTO.clrMsgTp();
        this.sttlmDt = clearingDTO.clrSysWorkDt().replaceAll("-","");
        this.batId = clearingDTO.clrBatId();
        this.bizTp = clearingDTO.clrBizTp();
        this.bizKind = clearingDTO.clrBizKind();
        this.transTp = clearingDTO.clrTransTp();
        this.sendPtyId = clearingDTO.clrSendPtyId();
        this.sendSysId = clearingDTO.clrSendSysId();
        this.recvPtyId = clearingDTO.clrRecvPtyId();
        this.recvSysId = clearingDTO.clrRecvSysId();
        this.dbtrPtyId = clearingDTO.clrDbtrPtyId();
        this.dbtrSysId = clearingDTO.clrDbtrSysId();
        this.dbtrId = clearingDTO.clrDbtrWltId();
        this.cbtrPtyId = clearingDTO.clrCdtrPtyId();
        this.cbtrSysId = clearingDTO.clrCdtrSysId();
        this.cbtrId = clearingDTO.clrCdtrWltId();
        this.currency = clearingDTO.clrCurrency();
        this.sttlmAmt = AmtUtils.toCents(clearingDTO.clrAmt());
        this.cdtDbtInd = clearing.cdtDbtInd().getCode();
        this.bizSts = ClearingProdCdtDbtIndEnum.DBIT.getCode().equals(clearing.cdtDbtInd().getCode())
                ? ClearingStatusEnum.PROCESS.getCode() : ClearingStatusEnum.WAIT_SETTLE.getCode();
        this.bizDt = clearingDTO.clrCreDtTm();
        this.clrFlag = clearingDTO.clrFlag();
        this.clrProd = clearing.clrProd().getCode();
        this.gmtCreate = new Date();
        this.gmtModified = new Date();
    }

    public SettlementProdDO(ClearingDTO clearingDTO, Clearing clearing, ErrorEnum errorEnum) {
        this.batId = clearingDTO.clrBatId();
        this.msgTp = clearingDTO.clrMsgTp();
        this.sttlmDt = clearingDTO.clrSysWorkDt().replaceAll("-","");
        setMsgId(clearingDTO.getClrMsgId());

        this.bizTp = clearingDTO.clrBizTp();
        this.bizKind = clearingDTO.clrBizKind();
        this.transTp = clearingDTO.clrTransTp();
        this.sendPtyId = clearingDTO.clrSendPtyId();
        this.sendSysId = clearingDTO.clrSendSysId();
        this.recvPtyId = clearingDTO.clrRecvPtyId();
        this.recvSysId = clearingDTO.clrRecvSysId();
        this.dbtrPtyId = clearingDTO.clrDbtrPtyId();
        this.dbtrSysId = clearingDTO.clrDbtrSysId();
        this.dbtrId = clearingDTO.clrDbtrWltId();
        this.cbtrPtyId = clearingDTO.clrCdtrPtyId();
        this.cbtrSysId = clearingDTO.clrCdtrSysId();
        this.cbtrId = clearingDTO.clrCdtrWltId();
        this.currency = clearingDTO.clrCurrency();
        this.sttlmAmt = AmtUtils.toCents(clearingDTO.clrAmt());
        this.cdtDbtInd = clearing.cdtDbtInd().getCode();
        this.bizSts = ClearingStatusEnum.FAILED.getCode();
        this.bizPrcCd = errorEnum.getCode();
        this.bizPrcInf = errorEnum.getDescription();
        this.bizDt = clearingDTO.clrCreDtTm();
        this.clrFlag = clearingDTO.clrFlag();
        this.clrProd = clearing.clrProd().getCode();
        this.gmtCreate = new Date();
        this.gmtModified = new Date();
    }

    public SettlementProdDO(Dcep19100101DTO dc191Dto, Clearing clearing, String bizSts) {
        for(CdtTrfTxInf txInf : dc191Dto.getCdtTrfTxInf()) {
            this.batId = dc191Dto.clrBatId(txInf);
            this.msgTp = dc191Dto.clrMsgTp();
            this.sttlmDt = dc191Dto.clrSysWorkDt().replaceAll("-", "");
            setMsgId(dc191Dto.getClrMsgId());

            this.bizTp = dc191Dto.clrBizTp(txInf);
            this.bizKind = dc191Dto.clrBizKind(txInf);
            this.transTp = dc191Dto.clrTransTp();
            this.sendPtyId = dc191Dto.clrSendPtyId();
            this.sendSysId = dc191Dto.clrSendSysId();
            this.recvPtyId = dc191Dto.clrRecvPtyId();
            this.recvSysId = dc191Dto.clrRecvSysId();
            this.dbtrPtyId = dc191Dto.clrDbtrPtyId(txInf);
            this.dbtrSysId = dc191Dto.clrDbtrSysId(txInf);
            this.dbtrId = dc191Dto.clrDbtrWltId(txInf);
            this.cbtrPtyId = dc191Dto.clrCdtrPtyId(txInf);
            this.cbtrSysId = dc191Dto.clrCdtrSysId(txInf);
            this.cbtrId = dc191Dto.clrCdtrWltId(txInf);
            this.currency = dc191Dto.clrCurrency(txInf);
            this.sttlmAmt = AmtUtils.toCents(dc191Dto.clrAmt(txInf));
            this.cdtDbtInd = clearing.cdtDbtInd().getCode();
            this.bizSts = bizSts;
            this.bizDt = dc191Dto.clrCreDtTm();
            this.clrFlag = dc191Dto.clrFlag();
            this.clrProd = clearing.clrProd().getCode();
            //TODO 待添加字段
//            this.rtrFlag = dc191Dto.clrFlag();
            this.gmtCreate = new Date();
            this.gmtModified = new Date();
        }
    }

    public SettlementProdDO(String msgId, String accountingStatus) {
        setGmtModified(new Date());
        setMsgId(msgId);
        this.bizSts = CommonUtil.actgStsToBizSts(accountingStatus);
    }

    public SettlementProdDO(String msgId, String accountingStatus, String bizPrcCd, String bizPrcInf) {
        setGmtModified(new Date());
        setMsgId(msgId);
        this.bizSts = CommonUtil.actgStsToBizSts(accountingStatus);
        this.bizPrcCd = bizPrcCd;
        this.bizPrcInf = bizPrcInf;
    }

    public SettlementProdDO(String msgId, ClearingStatusEnum clearingStatusEnum) {
        setGmtModified(new Date());
        setMsgId(msgId);
        this.bizSts = clearingStatusEnum.getCode();
    }

    public SettlementProdDO(String msgId, ClearingStatusEnum clearingStatusEnum, String bizPrcCd, String bizPrcInf) {
        setGmtModified(new Date());
        setMsgId(msgId);
        this.bizSts = clearingStatusEnum.getCode();
        this.bizPrcCd = bizPrcCd;
        this.bizPrcInf = bizPrcInf;
    }

    public SettlementProdDO(AccountingInstrDO accountingInstrDO) {
        setGmtModified(new Date());
        setMsgId(accountingInstrDO.getMsgId());
        this.bizPrcCd = accountingInstrDO.getActgPrcCd();
        this.bizPrcInf = accountingInstrDO.getActgPrcInf();
        switch (ActgStsEnum.getEnum(accountingInstrDO.getActgSts())) {
            case SUCCESS:
                this.bizSts = ClearingStatusEnum.WAIT_SETTLE.getCode();
                break;
            case FAILED:
                this.bizSts = ClearingStatusEnum.FAILED.getCode();
                break;
            case QUEUED:
                this.bizSts = ClearingStatusEnum.SETTLE_QUEUE.getCode();
                break;
            case QUEUE_CANCELED:
                this.bizSts = ClearingStatusEnum.CANCELLED.getCode();
                break;
            case QUEUE_RETURNED:
                this.bizSts = ClearingStatusEnum.DAYEND_RETURN.getCode();
                break;
            default:
                this.bizSts = ClearingStatusEnum.FAILED.getCode();
        }
    }

    public SettlementProdDO(ClearingDTO clearingDTO, Clearing clearing, String bizSts) {
        this.batId = clearingDTO.clrBatId();
        this.msgTp = clearingDTO.clrMsgTp();

        setMsgId(clearingDTO.getClrMsgId());

        this.bizTp = clearingDTO.clrBizTp();
        this.bizKind = clearingDTO.clrBizKind();
        this.transTp = clearingDTO.clrTransTp();
        this.sendPtyId = clearingDTO.clrSendPtyId();
        this.sendSysId = clearingDTO.clrSendSysId();
        this.recvPtyId = clearingDTO.clrRecvPtyId();
        this.recvSysId = clearingDTO.clrRecvSysId();
        this.dbtrPtyId = clearingDTO.clrDbtrPtyId();
        this.dbtrSysId = clearingDTO.clrDbtrSysId();
        this.dbtrId = clearingDTO.clrDbtrWltId();
        this.cbtrPtyId = clearingDTO.clrCdtrPtyId();
        this.cbtrSysId = clearingDTO.clrCdtrSysId();
        this.cbtrId = clearingDTO.clrCdtrWltId();
        this.currency = clearingDTO.clrCurrency();
        this.sttlmAmt = AmtUtils.toCents(clearingDTO.clrAmt());
        this.cdtDbtInd = clearing.cdtDbtInd().getCode();
        this.bizSts = bizSts;
        this.bizDt = clearingDTO.clrCreDtTm();
        this.clrFlag = clearingDTO.clrFlag();
        this.clrProd = clearing.clrProd().getCode();
        Date date = new Date();
        this.gmtCreate= date;
        this.gmtModified = date;
    }

    /**
     * 待结算PR12-->已结算PR10
     * @param msgId  报文编号
     * @param statusEnum 结算状态
     * @param sttlmDt 结算日期
     */
    public  SettlementProdDO(String msgId,ClearingStatusEnum statusEnum,String sttlmDt){
        setGmtModified(new Date());
        setMsgId(msgId);
        this.bizSts = statusEnum.getCode();
        this.sttlmDt = sttlmDt;
    }

    public SettlementProdDO(String sttlmDt,OnChainAdjustReqDTO onChainAdjustReqDTO) {
        setMsgId(onChainAdjustReqDTO.getMsgId());
        this.batId = onChainAdjustReqDTO.getBatchId();
        this.bizTp = onChainAdjustReqDTO.getBizTp();
        this.bizKind = onChainAdjustReqDTO.getBizKind();
        this.transTp = TransTpEnum.NORMAL_TRANS.getCode();
        this.sendPtyId = onChainAdjustReqDTO.getClearingMemberId();

        this.recvPtyId = InfoCacheUtil.getPbocInf();

        this.dbtrPtyId = onChainAdjustReqDTO.getClearingMemberId();


        this.cbtrPtyId = onChainAdjustReqDTO.getClearingMemberId();


        this.currency = onChainAdjustReqDTO.getCurrency();
        this.sttlmAmt = AmtUtils.toCents(onChainAdjustReqDTO.getAmount());

        this.bizSts = ClearingStatusEnum.WAIT_SETTLE.getCode() ;

        this.sttlmDt = sttlmDt;
        this.bizDt = LocalDateTimeUtil.format(LocalDateTime.now(),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        this.clrFlag = ClrFlgEnum.YES.getCode();

        if("QOT01".equals(onChainAdjustReqDTO.getAdjustTp())){
            this.cdtDbtInd = ClearingProdCdtDbtIndEnum.DBIT.getCode();
            this.dbtrId = null;
            this.cbtrId = null;
            this.sendSysId =  ChnlSysEnum.BCSP.getCode();
            this.recvSysId = ChnlSysEnum.DCEP.getCode();
            this.dbtrSysId = ChnlSysEnum.DCEP.getCode();
            this.cbtrSysId = ChnlSysEnum.BCSP.getCode();
            this.msgTp =  MsgTpEnum.CHAIN_REQUEST_UP.getCode();
        }else if ("QOT02".equals(onChainAdjustReqDTO.getAdjustTp())){
            this.cdtDbtInd = ClearingProdCdtDbtIndEnum.CRDT.getCode();
            this.dbtrId =  null;
            this.cbtrId = null;
            this.sendSysId =  ChnlSysEnum.BCSP.getCode();
            this.recvSysId = ChnlSysEnum.DCEP.getCode();
            this.dbtrSysId = ChnlSysEnum.BCSP.getCode();
            this.cbtrSysId = ChnlSysEnum.DCEP.getCode();
            this.msgTp = MsgTpEnum.CHAIN_REQUEST_DOWN.getCode();
        }

        this.clrProd = ClearingProdEnum.REALTIME.getCode();

        this.gmtCreate = new Date();
        this.gmtModified = new Date();
    }

    /**
     * 是否为终态
     * @return
     */
    public boolean finished() {
        return (!ClearingStatusEnum.PROCESS.getCode().equals(this.getBizSts())
                && !ClearingStatusEnum.ACCEPTED.getCode().equals(this.getBizSts())
                && !ClearingStatusEnum.WAIT_SETTLE.getCode().equals(this.getBizSts())
                && !ClearingStatusEnum.SETTLE_QUEUE.getCode().equals(this.getBizSts()));
    }

}
