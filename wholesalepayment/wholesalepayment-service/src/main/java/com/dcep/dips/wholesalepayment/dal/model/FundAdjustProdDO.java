package com.dcep.dips.wholesalepayment.dal.model;

import com.dcep.common.utils.DcepDateUtils;
import com.dcep.common.utils.MsgIdUtil;
import com.dcep.dips.common.constant.ChnlSysEnum;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.dto.funding.IncreaseReqDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.AmtUtils;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金调整产品表
 */
@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundAdjustProdDO {
    /**
     *   报文标识号 dcep181、dcep183 msgId
     */
    private String msgId;

    /**
     *   报文编号
     */
    private String msgTp;

    /**
     *   调整通知标识号 dcep185 msgId
     */
    private String reportMsgId;

    /**
     *   报文编号
     */
    private String reportMsgTp;

    /**
     *   结算日期 yyyyMMdd
     */
    private String sttlmDt;

    /**
     *   发起机构标识
     */
    private String sendPtyId;

    /**
     *   被调整机构标识
     */
    private String adjustPtyId;

    /**
     *   被调整机构系统标识 DCEP
     */
    private String adjustSysId;

    /**
     *   被调整机构钱包ID
     */
    private String adjustWltId;

    /**
     *   调整类型 "PRFD：注资 FFIC：预注资 TRBH：清零 FFRD：预注资调减 FDRD：调减"
     */
    private String adjustTp;

    /**
     *   调整金额 单位：分
     */
    private BigDecimal adjustAmt;

    /**
     *   交易币种
     */
    private String currency;

    /**
     *   借贷标识 C：贷记 D：借记
     */
    private String cdtDbtInd;

    /**
     *   业务状态 "PR02: 处理中 PR10: 已结算"
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
     *   账户余额
     */
    private BigDecimal accountBalance;

    /**
     *   账户标识 "A: A账户 B: B账户"
     */
    private String accountFlag;

    /**
     *   注资最低限额，单位：分
     */
    private BigDecimal ciLimit;

    /**
     *   轧差保证金额度，单位：分
     */
    private BigDecimal netQuota;

    /**
     *   环境信息
     */
    private String envInfo;

    /**
     *   创建时间
     */
    private Date gmtCreate;

    /**
     *   修改时间
     */
    private Date gmtModified;

    public FundAdjustProdDO(String msgId, String msgTp, String sttlmDt, ZeroOutReqDTO zeroOutReqDTO) {
        this.msgId = msgId;
        this.msgTp = msgTp;

        this.reportMsgId = msgId;
        this.reportMsgTp = msgTp;

        this.sttlmDt = sttlmDt;

        this.sendPtyId = InfoCacheUtil.getPbocInf();
        this.adjustSysId = ChnlSysEnum.DCEP.getCode();
        this.adjustPtyId = zeroOutReqDTO.getMemberId();
        this.adjustWltId = null;

        this.adjustTp = HvpsAdjTypEnum.ZERO_OUT.getCode();

        this.adjustAmt = zeroOutReqDTO.getAmount();
        this.currency = zeroOutReqDTO.getCurrency();
        this.cdtDbtInd = ClearingProdCdtDbtIndEnum.DBIT.getCode();
        this.bizSts = ClearingStatusEnum.ACCEPTED.getCode();
        this.bizPrcCd = null;
        this.bizPrcInf = null;

        // 业务处理时间
        this.bizDt = DcepDateUtils.getDcepDateStrNow();

        this.envInfo = CommonUtil.getEnvInfo();
        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    /**
     * 机构请求：注资调减、预注资调减
     * @param fundingDTO 资金调整DTO
     */
    public FundAdjustProdDO(String sttlmDt, FundingDTO fundingDTO) {
        this.msgId = fundingDTO.msgId();
        this.msgTp = fundingDTO.msgTp();

        this.reportMsgTp = MsgTpEnum.FUND_ADJUST_NOTICE.getCode();

        // TODO 防重判断，加唯一索引？
        this.reportMsgId = MsgIdUtil.randomMsgId(MsgTpEnum.FUND_ADJUST_NOTICE_ABBR.getCode(),
                CommonConstant.PBOC_SHORT_PTY_ID, CommonUtil.getEnvVal());

        this.sttlmDt = sttlmDt;

        this.sendPtyId = fundingDTO.sendPtyId();
        this.adjustSysId = ChnlSysEnum.DCEP.getCode();
        this.adjustPtyId = fundingDTO.adjustPtyId();
        this.adjustWltId = null;

        this.adjustTp = fundingDTO.adjustTp();

        this.adjustAmt = AmtUtils.toCents(fundingDTO.adjustAmt());
        this.currency = fundingDTO.currency();
        this.cdtDbtInd = ClearingProdCdtDbtIndEnum.DBIT.getCode();
        this.bizSts = ClearingStatusEnum.PROCESS.getCode();
        this.bizPrcCd = null;
        this.bizPrcInf = null;

        // 业务处理时间
        this.bizDt = fundingDTO.creDtTm();

        this.envInfo = CommonUtil.getEnvInfo();
        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    public FundAdjustProdDO(String msgId) {
        this.msgId = msgId;
    }

    public FundAdjustProdDO(String msgId, String bizSts, String bizPrcCd, String bizPrcInf) {
        this.msgId = msgId;
        this.bizSts = bizSts;
        this.bizPrcCd = bizPrcCd;
        this.bizPrcInf = bizPrcInf;
        this.gmtModified = new Date();
    }

    public FundAdjustProdDO(String msgId, String sttlmDt, IncreaseReqDTO increaseReqDTO) {
        this.msgId = msgId;
        this.msgTp = MsgTpEnum.FUND_ADJUST_NOTICE.getCode();
        this.reportMsgId = msgId;
        this.reportMsgTp = MsgTpEnum.FUND_ADJUST_NOTICE.getCode();

        this.sttlmDt = sttlmDt;

        this.sendPtyId = InfoCacheUtil.getInstNoForHvpsClrBkNo(increaseReqDTO.getClearingMemberId());
        this.adjustSysId = ChnlSysEnum.DCEP.getCode();
        this.adjustPtyId = InfoCacheUtil.getInstNoForHvpsBkNo(increaseReqDTO.getReceiveMemberId());
        this.adjustWltId = null;

        if (MsgTpEnum.CDT_FUND_INCREASE.getCode().equals(increaseReqDTO.getMsgTp())) {
            this.adjustTp = HvpsAdjTypEnum.INCREASE.getCode();
        } else if (MsgTpEnum.CDT_FUND_PRE_INCREASE.getCode().equals(increaseReqDTO.getMsgTp())) {
            this.adjustTp = HvpsAdjTypEnum.PRE_INCREASE.getCode();
        }

        this.adjustAmt = AmtUtils.toCents(increaseReqDTO.getAmount());
        this.currency = increaseReqDTO.getCurrency();
        this.cdtDbtInd = WholesaleCdtDbtIndEnum.CRDT.getCode();
        this.bizSts = ClearingStatusEnum.ACCEPTED.getCode();
        this.bizPrcCd = null;
        this.bizPrcInf = null;

        // 业务处理时间
        this.bizDt = DcepDateUtils.getDcepDateStrNow();

        this.envInfo = CommonUtil.getEnvInfo();
        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }
}
