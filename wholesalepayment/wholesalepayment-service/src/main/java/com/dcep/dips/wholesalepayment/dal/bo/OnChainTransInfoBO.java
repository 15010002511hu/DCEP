package com.dcep.dips.wholesalepayment.dal.bo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.Gateway;
import com.dcep.dips.common.constant.ChnlSysEnum;
import com.dcep.dips.wholesalepayment.common.utils.BatIdUtil;
import com.dcep.dips.wholesalepayment.common.utils.TimeUtil;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainTransReqDTO;
import com.dcep.gateway.mcbdc.dto.mcbs708.Mcbs70800101DTO;
import com.dcep.dips.wholesalepayment.enums.ActgBizTpEnum;
import com.dcep.dips.wholesalepayment.utils.AmtUtils;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnChainTransInfoBO implements RecordDTO, Serializable {
    private static final long serialVersionUID = -6834465658056191880L;
    /**
     * 报文标识号
     */
    private String msgId;

    /**
     * 报文编号
     */
    private String msgTp;

    /**
     * 交易时间
     */
    private String bizDt;

    /**
     * 交易批次号
     */
    private String batId;

    /**
     * 渠道系统
     */
    private String chnlSys;

    /**
     * 交易金额
     */
    private BigDecimal transAmt;

    /**
     * 业务类型
     */
    private String bizTp;

    /**
     * 交易状态
     */
    private String transSts;

    /**
     * 链上付款机构编码
     */
    private String chainDbtrPtyId;

    /**
     * 付款机构编码
     */
    private String dbtrPtyId;

    /**
     * 付款机构系统标识
     */
    private String dbtrSysId;

    /**
     * 付款机构LEI码
     */
    private String dbtrPtyLei;

    /**
     * 付款钱包ID
     */
    private String dbtrWltId;

    /**
     * 链上收款机构编码
     */
    private String chainCdtrPtyId;

    /**
     * 收款机构编码
     */
    private String cdtrPtyId;

    /**
     * 收款机构系统标识
     */
    private String cdtrSysId;

    /**
     * 收款机构LEI码
     */
    private String cdtrPtyLei;

    /**
     * 收款钱包ID
     */
    private String cdtrWltId;

    /**
     * 登记时间戳
     */
    private Date gmtCreate;

    /**
     * 更新时间戳
     */
    private Date gmtModified;

    /**
     * 记账业务类型
     */
    private String actgBizTp;

    /**
     * 使用系统标志
     */
    private String useCurrentSystemFlag;

    @Override
    public String encode() {
        return JSON.toJSONString(this);
    }

    @Override
    public RecordDTO decode(String encode) {
        return JSONObject.toJavaObject(JSON.parseObject(encode), this.getClass());
    }

    @Override
    public String recMsgId() {
        return null;
    }

    @Override
    public String recMsgTp() {
        return null;
    }

    @Override
    public String recOrgnlMsgId() {
        return null;
    }

    @Override
    public String recOrgnlMsgTp() {
        return null;
    }

    public OnChainTransInfoBO(OnChainTransReqDTO onChainTransReqDTO) {
        this.msgId = onChainTransReqDTO.getMsgId();
        this.msgTp = "msgTp";   // todo
        this.bizDt = TimeUtil.formatDateTime(onChainTransReqDTO.getBizDt());
        this.batId = onChainTransReqDTO.getBatId();
        this.chnlSys = onChainTransReqDTO.getChannelSys();
        this.transAmt = AmtUtils.toCents(onChainTransReqDTO.getTransAmt());
        this.bizTp = onChainTransReqDTO.getBizTp();
        this.transSts = onChainTransReqDTO.getTrans_sts();
        this.dbtrPtyId = onChainTransReqDTO.getDbtrPtyId();
        this.dbtrSysId = onChainTransReqDTO.getDbtrSysId();
        this.dbtrWltId = onChainTransReqDTO.getDbtrWltId();
        this.cdtrPtyId = onChainTransReqDTO.getCdtrPtyId();
        this.cdtrSysId = onChainTransReqDTO.getCdtrSysId();
        this.cdtrWltId = onChainTransReqDTO.getCdtrWltId();
        this.actgBizTp = ActgBizTpEnum.SWBC04.getCode();
        this.useCurrentSystemFlag = onChainTransReqDTO.getUseCurrentSystemFlag();
    }

    public OnChainTransInfoBO(Mcbs70800101DTO mcbs70800101DTO) {
        this.msgId = mcbs70800101DTO.getGrpHdr().getMsgId();
        this.msgTp = mcbs70800101DTO.getClass().getAnnotation(Gateway.class).msgTp();
        this.bizDt = mcbs70800101DTO.getTxInf().getDetailInf().getDeDtTm();
        this.batId = "B"+ BatIdUtil.trsTimeToBat(mcbs70800101DTO.getTxInf().getDetailInf().getDlTime()) + "00";
        this.chnlSys = ChnlSysEnum.MCBS.getCode();
        this.transAmt = AmtUtils.toCents(mcbs70800101DTO.getTxInf().getDetailInf().getAmt());
        this.bizTp = mcbs70800101DTO.getTxInf().getDetailInf().getBizTp();
        this.transSts = mcbs70800101DTO.getTxInf().getDetailInf().getStsCd();
        this.chainDbtrPtyId = mcbs70800101DTO.getTxInf().getDetailInf().getPayerAgt().getFinInstnId().getClrSysMmbId().getMmbId();
        this.dbtrPtyId = mcbs70800101DTO.getTxInf().getDetailInf().getPayerAgt().getFinInstnId().getClrSysMmbId().getMmbId();
        this.dbtrSysId = ChnlSysEnum.MCBS.getCode(); //付款机构系统标识赋值渠道
        this.dbtrPtyLei = mcbs70800101DTO.getTxInf().getDetailInf().getPayerAgt().getFinInstnId().getLei();
        this.dbtrWltId = mcbs70800101DTO.getTxInf().getDetailInf().getPayerAgt().getWltId();
        this.chainCdtrPtyId = mcbs70800101DTO.getTxInf().getDetailInf().getPayeeAgt().getFinInstnId().getClrSysMmbId().getMmbId();
        this.cdtrPtyId = mcbs70800101DTO.getTxInf().getDetailInf().getPayeeAgt().getFinInstnId().getClrSysMmbId().getMmbId();
        this.cdtrSysId = ChnlSysEnum.MCBS.getCode(); //收款机构系统标识赋值渠道
        this.cdtrPtyLei = mcbs70800101DTO.getTxInf().getDetailInf().getPayeeAgt().getFinInstnId().getLei();
        this.cdtrWltId = mcbs70800101DTO.getTxInf().getDetailInf().getPayeeAgt().getWltId();
        this.actgBizTp = ActgBizTpEnum.SWBC05.getCode();
    }
}
