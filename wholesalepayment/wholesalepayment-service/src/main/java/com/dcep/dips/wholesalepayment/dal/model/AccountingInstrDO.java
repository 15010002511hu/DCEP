package com.dcep.dips.wholesalepayment.dal.model;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.acctrans.constants.Constant;
import com.dcep.dips.common.constant.ChnlSysEnum;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.common.utils.PartitionUtil;
import com.dcep.dips.wholesalepayment.dal.bo.OnChainTransInfoBO;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustReqDTO;
import com.dcep.dips.wholesalepayment.dto.chain.ZeroOutResult;
import com.dcep.dips.wholesalepayment.dto.funding.IncreaseReqDTO;
import com.dcep.dips.wholesalepayment.dto.mcbs101.ClrDtlInf;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.AmtUtils;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.*;
import org.apache.dubbo.common.constants.CommonConstants;

import java.math.BigDecimal;
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
public class AccountingInstrDO {
    /**
     *   交易流水号 调用记账服务的交易流水号
     */
    private String transId;

    /**
     *   分区时间 数据库分区
     */
    private Date partitionTime;

    /**
     *   报文标识号 一笔msgId可对应多笔transId
     */
    private String msgId;

    /**
     *   报文编号
     */
    private String msgTp;

    /**
     *   原交易流水号 原调用记账服务的交易流水号
     */
    private String orgnlTransId;

    /**
     * Database Column Remarks:
     *   发起机构
     */
    private String sendPtyId;

    /**
     *   端到端流水号
     */
    private String endToEndId;

    /**
     *   记账业务类型 "SWBC01：流动性调拨业务 SWBC02：零售净额轧差业务 SWBC03：批发实时结算业务 SWBC04：区块链服务业务 SWBC05：货币桥业务 SWBC06：JISR业务"
     * @see ActgBizTpEnum
     */
    private String actgBizTp;

    /**
     *   记账业务种类
     */
    private String actgBizKind;

    /**
     *   业务优先级 "NORM：普通 HIGH：紧急 URGT：特急"
     * @see BizPrtyEnum
     */
    private String bizPrty;

    /**
     *   管理类型 "00：注资调增 01：注资调减 10：预注资调增 11：预注资调减 12：清零 20：入库 21：出库 30: 正常记账 31: 冻结记账 32: 解冻并记账"
     * @see ActgMgmtTpEnum
     */
    private String mgmtTp;

    /**
     *   业务日期
     */
    private String bizDt;

    /**
     *   记账日期 记账请求应答更新
     */
    private String actgDt;

    /**
     *   转出方系统标识
     */
    private String fromClrSysId;

    /**
     *   转出方机构标识
     */
    private String fromClrMmbId;

    /**
     *   转出方钱包
     */
    private String fromWlltId;

    /**
     *   转出方账号
     */
    private String fromAcctNo;

    /**
     *   转入方系统标识 冻结时选输
     */
    private String toClrSysId;

    /**
     *   转入方机构标识 冻结时选输
     */
    private String toClrMmbId;

    /**
     *   转入方钱包
     */
    private String toWlltId;

    /**
     *   转入方账号
     */
    private String toAcctNo;

    /**
     *   记账模式 1-实时记账
     * @see ActgModelEnum
     */
    private String actgModel;

    /**
     *   币种
     */
    private String currency;

    /**
     *   记账金额，单位：分
     */
    private BigDecimal amount;

    /**
     *   摘要码
     */
    private String abstractCd;

    /**
     *   摘要码描述
     */
    private String abstractDesc;

    /**
     * Database Column Remarks:
     *   记账状态 1-成功，记账请求应答更新
     * @see ActgStsEnum
     */
    private String actgSts;

    /**
     *   记账处理码
     */
    private String actgPrcCd;

    /**
     *   记账处理信息
     */
    private String actgPrcInf;

    /**
     *   创建时间
     */
    private Date gmtCreate;

    /**
     *   修改时间
     */
    private Date gmtModified;

    public void setTransId(String transId) {
        this.transId = transId == null ? null : transId.trim();
        this.partitionTime = PartitionUtil.partitionTime(this.transId);
    }

    public AccountingInstrDO(String msgId, String msgTp, String bizDt, ZeroOutReqDTO zeroOutReqDTO) {
        setTransId(zeroOutReqDTO.getTransId());

        this.msgId = msgId;
        this.msgTp = msgTp;
        this.sendPtyId = InfoCacheUtil.getPbocInf();
        this.endToEndId = msgId;

        this.actgBizTp = ActgBizTpEnum.SWBC01.getCode();
        this.actgBizKind = ActgBizKindEnum.DEFAULT.getCode();
        this.bizPrty = BizPrtyEnum.URGT.getCode();
        this.mgmtTp = ActgMgmtTpEnum.ZERO_OUT_DECR.getCode();

        this.bizDt = bizDt;
        this.actgDt = zeroOutReqDTO.getAccountingDate();

        this.fromClrSysId = ChnlSysEnum.DCEP.getCode();
        this.fromClrMmbId = zeroOutReqDTO.getMemberId();
        this.fromWlltId = null;
        this.fromAcctNo = null;

        this.toClrSysId = ChnlSysEnum.DCEP.getCode();
        this.toClrMmbId = InfoCacheUtil.getPbocInf();
        this.toWlltId = null;
        this.toAcctNo = null;

        this.actgModel = Constant.ActgMode.REAL_TIME;
        this.currency = zeroOutReqDTO.getCurrency();
        this.amount = zeroOutReqDTO.getAmount();

        this.abstractCd = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_LIQUIDITY).getParmVal();
        this.abstractDesc = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_LIQUIDITY).getParmDesc();

        this.actgSts = Constant.AccountingStatus.SUCCESS;
        this.actgPrcCd = null;
        this.actgPrcInf = null;

        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    public AccountingInstrDO(ZeroOutReqDTO zeroOutReqDTO) {
        setTransId(zeroOutReqDTO.getTransId());
    }

    public AccountingInstrDO(String transId) {
        setTransId(transId);
        this.gmtModified = new Date();
    }

    public AccountingInstrDO(String msgId, String orgnlTransId) {
        this.msgId = msgId;
        this.orgnlTransId = orgnlTransId;
        setGmtModified(new Date());
    }

    public AccountingInstrDO(String transId, String bizDt, FundAdjustProdDO fundAdjustProdDO, IncreaseReqDTO increaseReqDTO) {
        setTransId(transId);

        this.msgId = fundAdjustProdDO.getMsgId();
        this.orgnlTransId = transId;
        this.msgTp = fundAdjustProdDO.getMsgTp();
        this.sendPtyId = InfoCacheUtil.getInstNoForHvpsClrBkNo(increaseReqDTO.getClearingMemberId());
        this.endToEndId = transId;
        this.actgBizTp = ActgBizTpEnum.SWBC01.getCode();
        this.actgBizKind = ActgBizKindEnum.DEFAULT.getCode();
        this.bizPrty = BizPrtyEnum.URGT.getCode();

        if (MsgTpEnum.CDT_FUND_INCREASE.getCode().equals(increaseReqDTO.getMsgTp())) {
            this.mgmtTp = ActgMgmtTpEnum.CAP_INJECT_INCR.getCode();
            this.abstractCd = AbstractEnum.CAP_INJECT_INCR.getCode();
            this.abstractDesc = AbstractEnum.CAP_INJECT_INCR.getDescription();
        } else if (MsgTpEnum.CDT_FUND_PRE_INCREASE.getCode().equals(increaseReqDTO.getMsgTp())) {
            this.mgmtTp = ActgMgmtTpEnum.PRE_INJECT_INCR.getCode();
            this.abstractCd = AbstractEnum.PRE_INJECT_INCR.getCode();
            this.abstractDesc = AbstractEnum.PRE_INJECT_INCR.getDescription();
        }

        this.bizDt = bizDt;

        this.fromClrSysId = ChnlSysEnum.DCEP.getCode();
        this.fromClrMmbId = InfoCacheUtil.getPbocInf();
        this.fromWlltId = InfoCacheUtil.getPbocDcepWlltId();
        this.fromAcctNo = null;

        this.toClrSysId = ChnlSysEnum.DCEP.getCode();
        this.toClrMmbId = InfoCacheUtil.getInstNoForHvpsBkNo(increaseReqDTO.getReceiveMemberId());
        this.toWlltId = null;
        this.toAcctNo = null;

        this.actgModel = Constant.ActgMode.REAL_TIME;

        this.currency = fundAdjustProdDO.getCurrency();
        this.amount = fundAdjustProdDO.getAdjustAmt();

        this.abstractCd = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_LIQUIDITY).getParmVal();
        this.abstractDesc = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_LIQUIDITY).getParmDesc();

        this.actgSts = Constant.AccountingStatus.PROCESSING;
        this.actgPrcCd = null;
        this.actgPrcInf = null;

        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    public AccountingInstrDO(String transId, String bizDt, FundingDTO fundingDTO) {
        setTransId(transId);

        this.msgId = fundingDTO.msgId();
        this.msgTp = fundingDTO.msgTp();
        this.orgnlTransId = null;
        this.sendPtyId = fundingDTO.sendPtyId();
        this.endToEndId = fundingDTO.msgId();

        this.actgBizTp = fundingDTO.actgBizTp();
        this.actgBizKind = fundingDTO.actgBizKind();
        this.bizPrty = fundingDTO.bizPrty();
        this.mgmtTp = fundingDTO.mgmtTp();

        this.bizDt = bizDt;
        this.actgDt = null;

        this.fromClrSysId = ChnlSysEnum.DCEP.getCode();
        this.fromClrMmbId = fundingDTO.dbtrPtyId();
        this.fromWlltId = null;
        this.fromAcctNo = null;

        this.toClrSysId = ChnlSysEnum.DCEP.getCode();
        this.toClrMmbId = InfoCacheUtil.getPbocInf();

        this.toWlltId = InfoCacheUtil.getPbocDcepWlltId();
        this.toAcctNo = null;

        this.actgModel = Constant.ActgMode.REAL_TIME;

        this.currency = fundingDTO.currency();
        this.amount = AmtUtils.toCents(fundingDTO.adjustAmt());

        this.abstractCd = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_LIQUIDITY).getParmVal();
        this.abstractDesc = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_LIQUIDITY).getParmDesc();

        this.actgSts = Constant.AccountingStatus.PROCESSING;
        this.actgPrcCd = null;
        this.actgPrcInf = null;

        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    public AccountingInstrDO(String transId, AccountingInstrDO prepareAccInstrDO) {
        setTransId(transId);
        this.msgId = prepareAccInstrDO.getMsgId();
        this.msgTp = MsgTpEnum.FUND_ADJUST_NOTICE.getCode();
        this.orgnlTransId = prepareAccInstrDO.getTransId();
        this.endToEndId = transId;

        this.abstractCd = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_LIQUIDITY).getParmVal();
        this.abstractDesc = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_LIQUIDITY).getParmDesc();

        this.actgSts = Constant.AccountingStatus.PROCESSING;
        this.sendPtyId = prepareAccInstrDO.getSendPtyId();
        this.actgBizTp = prepareAccInstrDO.getActgBizTp();
        this.actgBizKind = prepareAccInstrDO.getActgBizKind();
        this.bizPrty = prepareAccInstrDO.getBizPrty();
        this.mgmtTp = prepareAccInstrDO.getMgmtTp();

        // 复用冻结时刻的系统日期，还是此时系统状态表中的系统日期
        this.bizDt = prepareAccInstrDO.getBizDt();

        this.fromClrSysId = prepareAccInstrDO.getFromClrSysId();
        this.fromClrMmbId = prepareAccInstrDO.getFromClrMmbId();
        this.fromWlltId = prepareAccInstrDO.getFromWlltId();
        this.fromAcctNo = prepareAccInstrDO.getFromAcctNo();

        this.toClrSysId = prepareAccInstrDO.getToClrSysId();
        this.toClrMmbId = prepareAccInstrDO.getToClrMmbId();
        this.toWlltId = prepareAccInstrDO.getToWlltId();
        this.toAcctNo = prepareAccInstrDO.getToAcctNo();

        this.actgModel = prepareAccInstrDO.getActgModel();
        this.currency = prepareAccInstrDO.getCurrency();
        this.amount = prepareAccInstrDO.getAmount();

        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    public AccountingInstrDO(String transId, OnChainTransInfoBO onChainTransInfoBO, String curSysDt) {
        setTransId(transId);
        this.msgId = onChainTransInfoBO.getMsgId();
        this.msgTp = onChainTransInfoBO.getMsgTp();   // todo 链上同步记账报文没有msgTp,待定
        this.sendPtyId = onChainTransInfoBO.getChnlSys(); // 发起机构赋值渠道
        this.endToEndId = onChainTransInfoBO.getMsgId();
        this.actgBizTp = onChainTransInfoBO.getActgBizTp();
        this.actgBizKind = ActgBizKindEnum.DEFAULT.getCode();   // todo
        this.bizPrty = BizPrtyEnum.NORM.getCode();
        this.mgmtTp = ActgMgmtTpEnum.NORMAL.getCode();
        this.bizDt = DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN);
        this.actgDt = curSysDt; // todo
        this.fromClrSysId = onChainTransInfoBO.getDbtrSysId(); // 转出方系统标识赋值渠道
        this.fromClrMmbId = onChainTransInfoBO.getDbtrPtyId();
        this.fromWlltId = onChainTransInfoBO.getDbtrWltId();
        this.toClrSysId = onChainTransInfoBO.getCdtrSysId(); // 转入方系统标识赋值渠道
        this.toClrMmbId = onChainTransInfoBO.getCdtrPtyId();
        this.toWlltId = onChainTransInfoBO.getCdtrWltId();
        this.actgModel = Constant.ActgMode.REAL_TIME;
        this.currency = "CNY"; // todo CNY?
        this.amount = onChainTransInfoBO.getTransAmt();
        this.abstractCd = "1"; // todo 摘要吗
        this.abstractDesc = "abstractDesc"; // todo 摘要描述
        this.actgSts = Constant.AccountingStatus.PROCESSING;
        this.gmtCreate = new Date();
        this.gmtModified = new Date();
    }

    public AccountingInstrDO(String transId, ClearingDTO clearingDTO, Clearing clearing) {
        setTransId(transId);

        this.msgId = clearingDTO.getClrMsgId();
        this.msgTp = clearingDTO.clrMsgTp();
        this.sendPtyId = clearingDTO.clrSendPtyId();
        this.endToEndId = clearingDTO.clrEndToEndId() == null ? clearingDTO.getClrMsgId() : clearingDTO.clrEndToEndId();
        this.actgBizTp = clearing.actgBizTp().getCode();
        //TODO actgBizKind
        this.actgBizKind = clearingDTO.clrBizKind();
        this.bizPrty = clearingDTO.clrBizPrty();
        this.mgmtTp = ActgMgmtTpEnum.NORMAL.getCode();
        // 业务日期取当前系统日期
        this.bizDt = DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN);

        this.fromClrSysId = clearingDTO.clrDbtrSysId();
        this.fromClrMmbId = clearingDTO.clrDbtrPtyId();
        // fromWlltId
        // fromAcctNo
        this.toClrSysId = clearingDTO.clrCdtrSysId();
        this.toClrMmbId = clearingDTO.clrCdtrPtyId();
        // toWlltId
        // toAcctNo
        //TODO ??
        this.actgModel = Constant.ActgMode.REAL_TIME;
        this.currency = clearingDTO.clrCurrency();
        this.amount = AmtUtils.toCents(clearingDTO.clrAmt());

        this.abstractCd = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_WHOLESALE).getParmVal();
        this.abstractDesc = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_WHOLESALE).getParmDesc();

        this.actgSts = Constant.AccountingStatus.PROCESSING;
        //TODO ??
        this.actgPrcCd = "";
        this.actgPrcInf = "";
        this.gmtCreate = new Date();
        this.gmtModified = new Date();
    }

    /**
     * 区块链-清零结果通知
     * @param msgId
     * @param transId
     * @param zeroOutResult
     */
    public AccountingInstrDO(String msgId, String transId, ZeroOutResult zeroOutResult,String curSysDt) {
        setTransId(transId);

        this.msgId = msgId;
        this.msgTp = MsgTpEnum.ZERO_OUT_NOTICE_BCSP.getCode();
        this.orgnlTransId = null;

        this.sendPtyId = InfoCacheUtil.getPbocInf();
        this.endToEndId = msgId;

        this.actgBizTp = ActgBizTpEnum.SWBC04.getCode();
        this.actgBizKind = ActgBizKindEnum.DEFAULT.getCode();
        this.bizPrty = BizPrtyEnum.URGT.getCode();
        this.mgmtTp = ActgMgmtTpEnum.ZERO_OUT_DECR.getCode();

        this.bizDt = curSysDt;
        this.actgDt = null;

        this.fromClrSysId = ChnlSysEnum.BCSP.getCode();
        this.fromClrMmbId = zeroOutResult.getPtyId();
        this.fromWlltId = null;
        this.fromAcctNo = null;

        this.toClrSysId = ChnlSysEnum.DCEP.getCode();
        this.toClrMmbId = zeroOutResult.getPtyId();
        this.toWlltId = null;
        this.toAcctNo = null;

        this.actgModel = ActgModelEnum.REAL_TIME.getCode();
        this.currency = "CNY";
        this.amount = zeroOutResult.getFinishZeroOutAmt();

        this.abstractCd = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_BCSP).getParmVal();
        this.abstractDesc = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_BCSP).getParmDesc();

        this.actgSts = ActgStsEnum.PROCESS.getCode();
        this.actgPrcCd = null;
        this.actgPrcInf = null;

        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    /**
     * 货币桥-清零结果通知
     * @param msgId
     * @param transId
     * @param crDtlInf
     */
    public AccountingInstrDO(String msgId, String transId, ClrDtlInf crDtlInf,String curSysDt) {
        setTransId(transId);

        this.msgId = msgId;
        this.msgTp = MsgTpEnum.ZERO_OUT_NOTICE_MCBS.getCode();
        this.orgnlTransId = null;
        this.sendPtyId = InfoCacheUtil.getPbocInf();
        this.endToEndId = msgId;

        this.actgBizTp = ActgBizTpEnum.SWBC05.getCode();
        this.actgBizKind = ActgBizKindEnum.DEFAULT.getCode();
        this.bizPrty = BizPrtyEnum.URGT.getCode();
        this.mgmtTp = ActgMgmtTpEnum.ZERO_OUT_DECR.getCode();

        this.bizDt = curSysDt;
        this.actgDt = null;

        this.fromClrSysId = ChnlSysEnum.MCBS.getCode();
        this.fromClrMmbId = crDtlInf.getFinInsTnId().getClrSysMmbId().getMmbId();
        this.fromWlltId = null;
        this.fromAcctNo = null;

        this.toClrSysId = ChnlSysEnum.DCEP.getCode();
        this.toClrMmbId = crDtlInf.getFinInsTnId().getClrSysMmbId().getMmbId();
        this.toWlltId = null;
        this.toAcctNo = null;

        this.actgModel = ActgModelEnum.REAL_TIME.getCode();
        this.currency = crDtlInf.getFishClrAmt().getCcy();
        this.amount = new BigDecimal(crDtlInf.getFishClrAmt().getValue());

        this.abstractCd = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_MCBS).getParmVal();
        this.abstractDesc = InfoCacheUtil.getPublicParam(CommonConstant.ABS_CODE_REALTIME_MCBS).getParmDesc();

        this.actgSts = ActgStsEnum.PROCESS.getCode();
        this.actgPrcCd = null;
        this.actgPrcInf = null;

        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    public AccountingInstrDO(String transId, EnvelopeDTO<GwDTO> gwReqDTO) {
        ClearingDTO clearingDTO = (ClearingDTO) gwReqDTO.body();
        Clearing clearing = clearingDTO.getClass().getAnnotation(Clearing.class);

        setTransId(transId);

        this.msgId = clearingDTO.getClrMsgId();
        this.msgTp = clearingDTO.clrMsgTp();
        this.sendPtyId = clearingDTO.clrSendPtyId();
        this.endToEndId = clearingDTO.clrEndToEndId();
        this.actgBizTp = clearing.actgBizTp().getCode();
        //TODO actgBizKind
        this.actgBizKind = clearingDTO.clrBizKind();
        this.bizPrty = clearingDTO.clrBizPrty();
        this.mgmtTp = ActgMgmtTpEnum.NORMAL.getCode();
        this.bizDt = DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN);;

        this.fromClrSysId = clearingDTO.clrDbtrSysId();
        this.fromClrMmbId = clearingDTO.clrDbtrPtyId();
        this.fromWlltId = clearingDTO.clrDbtrWltId();
        this.fromAcctNo = null;
        this.toClrSysId = clearingDTO.clrCdtrSysId();
        this.toClrMmbId = clearingDTO.clrCdtrPtyId();
        this.toWlltId = clearingDTO.clrCdtrWltId();
        this.toAcctNo = null;
        this.actgModel = Constant.ActgMode.REAL_TIME;
        this.currency = clearingDTO.clrCurrency();
        this.amount = AmtUtils.toCents(clearingDTO.clrAmt());
        // todo 摘要吗
        this.abstractCd = "1";
        // todo 摘要描述
        this.abstractDesc = "testForTransferAPI";
        this.actgSts = Constant.AccountingStatus.PROCESSING;
        //TODO ??
        this.actgPrcCd = "";
        this.actgPrcInf = "";
        this.gmtCreate = new Date();
        this.gmtModified = new Date();
    }

    public AccountingInstrDO(String transId, OnChainAdjustReqDTO onChainAdjustReqDTO) {
        setTransId(transId);

        this.fromClrMmbId = onChainAdjustReqDTO.getClearingMemberId();
        this.fromAcctNo = null;
        this.toClrMmbId = onChainAdjustReqDTO.getClearingMemberId();
        this.toAcctNo = null;

        if(onChainAdjustReqDTO.getAdjustTp().equals("QOT01")){
            this.fromWlltId = null;
            this.toWlltId = null;
            this.msgTp =  MsgTpEnum.CHAIN_REQUEST_UP.getCode();
            this.fromClrSysId =  ChnlSysEnum.DCEP.getCode();
            this.toClrSysId = ChnlSysEnum.BCSP.getCode();
        }else if (onChainAdjustReqDTO.getAdjustTp().equals("QOT02")){
            this.fromWlltId = null;
            this.toWlltId = null;
            this.msgTp = MsgTpEnum.CHAIN_REQUEST_DOWN.getCode();
            this.fromClrSysId =  ChnlSysEnum.BCSP.getCode();
            this.toClrSysId = ChnlSysEnum.DCEP.getCode();
        }

        this.msgId = onChainAdjustReqDTO.getMsgId();
        this.sendPtyId = onChainAdjustReqDTO.getClearingMemberId();
        this.endToEndId = onChainAdjustReqDTO.getMsgId();
        this.actgBizTp = ActgBizTpEnum.SWBC04.getCode();
        this.actgBizKind = ActgBizKindEnum.BIZ_KIND_BCSP.getCode();
        this.bizPrty = BizPrtyEnum.NORM.getCode();
        this.mgmtTp = ActgMgmtTpEnum.NORMAL.getCode();
        this.bizDt = DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN);
        this.actgModel = Constant.ActgMode.REAL_TIME;
        this.currency = onChainAdjustReqDTO.getCurrency();
        this.amount = AmtUtils.toCents(onChainAdjustReqDTO.getAmount());
        // todo 摘要吗
        this.abstractCd = "00000000";
        // todo 摘要描述
        this.abstractDesc = "链上交易通知";
        this.actgSts = Constant.AccountingStatus.PROCESSING;
        this.gmtCreate = new Date();
        this.gmtModified = new Date();
    }

}
