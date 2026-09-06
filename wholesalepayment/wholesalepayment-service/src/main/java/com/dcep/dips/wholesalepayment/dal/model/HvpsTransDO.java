package com.dcep.dips.wholesalepayment.dal.model;

import com.dcep.clearingcenter.dto.settlement.HvpsReqDTO;
import com.dcep.clearingcenter.dto.settlement.HvpsRspDTO;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.common.util.DateUtils;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.dto.funding.IncreaseReqDTO;
import com.dcep.dips.wholesalepayment.dto.hvps.ClearReportReqDTO;
import com.dcep.dips.wholesalepayment.enums.HvpsBizKindEnum;
import com.dcep.dips.wholesalepayment.enums.HvpsBizTpEnum;
import com.dcep.dips.wholesalepayment.enums.HvpsClearingStatusEnum;
import com.dcep.dips.wholesalepayment.utils.AmtUtils;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

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
public class HvpsTransDO {
    /**
     *   大额报文标识号
     */
    private String hvpsMsgId;

    /**
     *   大额报文发起机构编码
     */
    private String hvpsSendPty;

    /**
     *   大额报文编号 "hvps.112/115/118"
     */
    private String hvpsMsgTp;

    /**
     *   报文标识号 关联资金调整产品表msg_id
     */
    private String msgId;

    /**
     *   端到端流水号
     */
    private String endToEndId;

    /**
     *   报文发送时间
     */
    private String sndDt;

    /**
     *   业务类型编码 大额业务类型编码
     */
    private String bizTp;

    /**
     *   业务种类编码 大额业务种类编码
     */
    private String bizKind;

    /**
     *   付款清算行机构标识
     */
    private String dbtrClrPtyId;

    /**
     *   付款机构编码
     */
    private String dbtrPtyId;

    /**
     *   收款清算行机构标识
     */
    private String cdtrClrPtyId;

    /**
     *   收款机构编码
     */
    private String cdtrPtyId;

    /**
     *   交易币种
     */
    private String currency;

    /**
     *   结算金额 单位：分
     */
    private BigDecimal sttlmAmt;

    /**
     *   结算日期 大额结算日期
     */
    private String sttlmDt;

    /**
     *   业务状态 大额业务状态
     */
    private String bizSts;

    /**
     *   业务处理码 大额业务处理码
     */
    private String bizPrcCd;

    /**
     *   业务处理信息 大额业务处理信息
     */
    private String bizPrcInf;

    /**
     *   登记时间戳
     */
    private Date gmtCreate;

    /**
     *   更新时间戳
     */
    private Date gmtModified;

    public HvpsTransDO(IncreaseReqDTO increaseReqDTO) {
        this.hvpsMsgId = increaseReqDTO.getMsgId();
        this.hvpsSendPty = increaseReqDTO.getSendMemberId();
        this.gmtModified = new Date();
    }

    public HvpsTransDO(ClearReportReqDTO reportReqDTO) {
        this.hvpsMsgId = reportReqDTO.getOrgnlMsgId();
        this.hvpsSendPty = reportReqDTO.getOrgnlSendPty();
        this.gmtModified = new Date();
    }

    public HvpsTransDO(String bizSts, String bizPrcCd, String bizPrcInf, String hvpsSettleDate) {
        this.bizSts = bizSts;
        this.bizPrcCd = bizPrcCd;
        this.bizPrcInf = bizPrcInf;
        this.sttlmDt = hvpsSettleDate;
        this.gmtModified = new Date();
    }

    public HvpsTransDO(IncreaseReqDTO increaseReqDTO, String msgId) {
        this.hvpsMsgId = increaseReqDTO.getMsgId();
        this.hvpsSendPty = increaseReqDTO.getSendMemberId();
        this.hvpsMsgTp = increaseReqDTO.getMsgTp();
        this.msgId = msgId;
        this.endToEndId = increaseReqDTO.getEndToEndId();
        this.sndDt = DcepDateUtils.getDcepDateStrNow();
        this.bizTp = HvpsBizTpEnum.INTER_BANK_TRANS.getCode();
        this.bizKind = HvpsBizKindEnum.INNER_TRANS.getCode();

        this.dbtrClrPtyId = increaseReqDTO.getClearingMemberId(); // 调增发起机构总行在大额的直参行号
        this.dbtrPtyId = increaseReqDTO.getSendMemberId();        // 调增发起机构分行在大额的间参行号
        this.cdtrClrPtyId = InfoCacheUtil.getPbocHvpsClrBkNo();   // 运营中心在大额的直参行号
        this.cdtrPtyId = increaseReqDTO.getReceiveMemberId();     // 被调增机构在大额的间参行号(运营中心下)

        this.currency = increaseReqDTO.getCurrency();
        this.sttlmAmt = AmtUtils.toCents(increaseReqDTO.getAmount());

        if (!StringUtils.isEmpty(increaseReqDTO.getAccountingDate())) {
            this.sttlmDt = DateUtils.convertToDatabase(increaseReqDTO.getAccountingDate());
        }

        this.bizSts = HvpsClearingStatusEnum.ACCEPTED.getCode();
        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    public HvpsTransDO(HvpsReqDTO hvpsReqDTO, HvpsRspDTO hvpsRspDTO) {
        this.hvpsMsgId = hvpsRspDTO.getHvpsMsgId();
        this.hvpsSendPty = hvpsReqDTO.getDbtrBrnchId();
        this.hvpsMsgTp = hvpsReqDTO.getMsgTp();

        this.msgId = hvpsReqDTO.getEndToEndId();
        this.endToEndId = hvpsReqDTO.getEndToEndId();
        this.sndDt = DcepDateUtils.getDcepDateStrNow();

        this.bizTp = HvpsBizTpEnum.INTER_BANK_TRANS.getCode();
        this.bizKind = HvpsBizKindEnum.INNER_TRANS.getCode();

        this.dbtrClrPtyId = hvpsReqDTO.getDbtrClearingMemberId();
        this.dbtrPtyId = hvpsReqDTO.getDbtrBrnchId();
        this.cdtrClrPtyId = hvpsReqDTO.getCdtrClearingMemberId();
        this.cdtrPtyId = hvpsReqDTO.getCdtrBranchId();

        this.currency = hvpsReqDTO.getCurrency();
        this.sttlmAmt = AmtUtils.toCents(hvpsReqDTO.getAmount());

        this.bizSts = HvpsClearingStatusEnum.ACCEPTED.getCode();
        this.gmtCreate = new Date();
        this.gmtModified = this.gmtCreate;
    }

    public HvpsTransDO(String hvpsMsgId, HvpsClearingStatusEnum statusEnum) {
        this.hvpsMsgId = hvpsMsgId;
        this.bizSts = statusEnum.getCode();
        this.gmtModified = new Date();
    }
}
