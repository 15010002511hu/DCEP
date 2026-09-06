package com.dcep.dips.wholesalepayment.dto.dc181;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.annotation.GwMethod;
import com.dcep.common.annotation.RpcInfo;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.annotation.WholesalePayment;
import com.dcep.dips.wholesalepayment.constants.Constant;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.dc183.MsgHdr;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * dcep.181.001.01DTO
 * @Author luteng
 * @date 2025-09-05 16:32:10
 */
@JacksonXmlRootElement(localName = "LqdtyDbtTrf", namespace = "http://www.dcep.com/dcep/18100101/")
@Setter
@Getter
@ToString(callSuper = true)
@Gateway(msgTp = "dcep.181.001.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {
        @RpcInfo(name = Constant.NAME_FUNDING, methods = { @GwMethod(name = Constant.METHOD_DECREASE) })}))
//@WholesalePayment(presumeStatus = ClearingStatusEnum.FAILED, checkMode = WholesaleCheckModeEnum.PAYER_CHECK,
//        confirmTimeout = 300, notityTimeout = 300)
@Record(saveMode = RecordSaveModeEnum.ALL)
public class Dcep18100101DTO extends GwDTO implements FundingDTO {
    /**
     * 组件MessageHeader
     */
    @JacksonXmlProperty(
            localName = "MsgHdr"
    )
    @NotNull
    @Valid
    private MsgHdr msgHdr;

    /**
     * 组件LiquidityDebitTransfer
     */
    @JacksonXmlProperty(localName = "LqdtyDbtTrf")
    @NotNull
    @Valid
    private LqdtyDbtTrf lqdtyDbtTrf;

    @Override
    public void init() {
    }

    @Override
    public String fetchMsgId() {
        return msgHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        // 报文头报文标识号
        String headerMsgId = soapHeader.getMsgSN().substring(0, 32);
        // 报文体报文标识号
        String bodyMsgId = msgHdr.getMsgId();
        if (!headerMsgId.equals(bodyMsgId)) {
            throw new DcepException(com.dcep.common.enums.ErrorEnum.MSGSN_MSGID_NOT_MATCH_ERROR);
        }

        // 校验msgId的(13,16)这3位与msgTp的中间3位是否一致 报文编号校验11
        String bodyMsgIdMsgTp = bodyMsgId.substring(12, 15);
        String msgTpNum = soapHeader.getMsgTp().substring(5, 8);
        if (!msgTpNum.equals(bodyMsgIdMsgTp)) {
            throw new DcepException(com.dcep.common.enums.ErrorEnum.MSGTP_NOT_IN_MSGID_ERROR);
        }

        String sender = soapHeader.getSender();
        String sendPtyId = this.sendPtyId();
        String dbtrPtyId = this.dbtrPtyId();

        if (!sender.equals(sendPtyId)) {
            throw new DcepException(WholesaleErrorEnum.HEADER_BODY_SEND_PTY_ID_NOT_MATCH_ERROR.getCode(),
                    WholesaleErrorEnum.HEADER_BODY_SEND_PTY_ID_NOT_MATCH_ERROR.getDescription());
        }

        if (!sendPtyId.equals(dbtrPtyId)) {
            throw new DcepException(WholesaleErrorEnum.BODY_SEND_PTY_ID_NOT_MATCH_ERROR.getCode(),
                    WholesaleErrorEnum.BODY_SEND_PTY_ID_NOT_MATCH_ERROR.getDescription());
        }

        return true;
    }

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
        return msgHdr.getMsgId();
    }

    @Override
    public String recMsgTp() {
        return MsgTpEnum.CDT_FUND_DECREASE.getCode();
    }

    @Override
    public String recOrgnlMsgId() {
        return null;
    }

    @Override
    public String recOrgnlMsgTp() {
        return null;
    }

    @Override
    public String hvpsMsgTp()  {
        return MsgTpEnum.CDT_FUND_DECREASE_OUT.getCode();
    }

    @Override
    public String hvpsBizTp() {
        return HvpsBizTpEnum.INTER_BANK_TRANS.getCode();
    }

    @Override
    public String hvpsBizKind() {
        return HvpsBizKindEnum.INNER_TRANS.getCode();
    }

    @Override
    public String actgBizTp() {
        return ActgBizTpEnum.SWBC01.getCode();
    }

    @Override
    public String actgBizKind() {
        return ActgBizKindEnum.DEFAULT.getCode();
    }

    @Override
    public String endToEndId() {
        return msgHdr.getMsgId();
    }

    @Override
    public String bizPrty() {
        return BizPrtyEnum.URGT.getCode();
    }

    @Override
    public String abstractCd() {
        return AbstractEnum.CAP_INJECT_DECR.getCode();
    }

    @Override
    public String abstractDesc() {
        return AbstractEnum.CAP_INJECT_DECR.getDescription();
    }

    @Override
    public String msgTp() {
        return MsgTpEnum.CDT_FUND_DECREASE.getCode();
    }

    @Override
    public String adjustPtyId() {
        return lqdtyDbtTrf.getDbtr().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String adjustTp() {
        return HvpsAdjTypEnum.DECREASE.getCode();
    }

    @Override
    public String adjustAmt() {
        return lqdtyDbtTrf.getTrfdAmt().getAmtWthCcy().getValue();
    }

    @Override
    public String currency() {
        return lqdtyDbtTrf.getTrfdAmt().getAmtWthCcy().getCcy();
    }

    @Override
    public String msgId() {
        return msgHdr.getMsgId();
    }

    @Override
    public String mgmtTp() {
        return ActgMgmtTpEnum.CAP_INJECT_DECR.getCode();
    }

    @Override
    public String sendPtyId() {
        return lqdtyDbtTrf.getCdtr().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String recvPtyId() {
        return InfoCacheUtil.getPbocInf();
    }

    @Override
    public String dbtrPtyId() {
        return lqdtyDbtTrf.getDbtr().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String cdtDbtInd() {
        return WholesaleCdtDbtIndEnum.CRDT.getCode();
    }

    @Override
    public String creDtTm() {
        return msgHdr.getCreDtTm();
    }
}
