package com.dcep.dips.wholesalepayment.dto.dc427;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.annotation.GwMethod;
import com.dcep.common.annotation.RpcInfo;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.ValidateUtils;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.constants.Constant;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.dto.common.Common;
import com.dcep.dips.wholesalepayment.dto.dc201.Cdtr;
import com.dcep.dips.wholesalepayment.dto.dc201.RmtInf;
import com.dcep.dips.wholesalepayment.dto.dc201.SchmeNm;
import com.dcep.dips.wholesalepayment.dto.dc201.Tp;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.DtoCheckUtil;
import com.dcep.dips.wholesalepayment.validation.CheckClrBatId;
import com.dcep.infocache.manager.NacosConsume;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * dcep.427.001.01DTO
 * @Author luteng
 * @date 2025-10-21 16:31:44
 */
@JacksonXmlRootElement(
        localName = "FIToFIPmtCxlReq",
        namespace = "http://www.dcep.com/dcep/42700101/"
)

@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Gateway(msgTp = "dcep.427.001.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {
        @RpcInfo(name = Constant.NAME_REVERSE, methods = { @GwMethod(name = Constant.METHOD_REVERSE_PROCESS) }) }))
@Clearing(presumeStatus = ClearingStatusEnum.NONE, action = ClearingActionEnum.NONE, confirmTimeout = 300, instgDrctPty = InstgDrctPtyEnum.NONE, checkMode = ClearingCheckModeEnum.NON_CHECK, cdtDbtInd = ClearingProdCdtDbtIndEnum.NONE)
@Record(saveMode = RecordSaveModeEnum.ALL)
@CheckClrBatId
public class Dcep42700101DTO extends GwDTO  implements ClearingDTO, DataEncryption {
    /**
     * 组件Assignment
     */
    @JacksonXmlProperty(
            localName = "Assgnmt"
    )
    @NotNull
    @Valid
    private Assgnmt assgnmt;

    /**
     * 组件Underlying
     */
    @JacksonXmlProperty(
            localName = "Undrlyg"
    )
    @NotNull
    @Valid
    private Undrlyg undrlyg;

    @Override
    public void init() {
    }

    @Override
    public String fetchMsgId() {
        return null;
    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        // 报文头报文标识号
        String headerMsgId = soapHeader.getMsgSN().substring(0, 32);
        // 报文体报文标识号
        String bodyMsgId = assgnmt.getId();
        if (!headerMsgId.equals(bodyMsgId)) {
            throw new DcepException(com.dcep.common.enums.ErrorEnum.MSGSN_MSGID_NOT_MATCH_ERROR);
        }

        // 校验msgId的(13,16)这3位与msgTp的中间3位是否一致 报文编号校验11
        String bodyMsgIdMsgTp = bodyMsgId.substring(12, 15);
        String msgTpNum = soapHeader.getMsgTp().substring(5, 8);
        if (!msgTpNum.equals(bodyMsgIdMsgTp)) {
            throw new DcepException(com.dcep.common.enums.ErrorEnum.MSGTP_NOT_IN_MSGID_ERROR);
        }
        return true;
    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {

    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {

    }

    @Override
    public void clrBatId(String clrBatId) {

    }

    @Override
    public String clrBatId() {
        return "";
    }

    @Override
    public String clrTransTp() {
        return TransTpEnum.NONE.getCode();
    }

    @Override
    public String clrMsgTp() {
        return  MsgTpEnum.REVERSE_REQUREST.getCode();
    }

    @Override
    public String clrEndToEndId() {
        return assgnmt.getId();
    }

    @Override
    public String getClrMsgId() {
        return assgnmt.getId();
    }


    @Override
    public String clrBizTp() {
        return undrlyg.getTxInf().getOrgnlTxRef().getPmtTpInf().getCtgyPurp().getPrtry();
    }

    @Override
    public String clrBizKind() {
        return undrlyg.getTxInf().getOrgnlTxRef().getPurp().getPrtry();
    }

    @Override
    public String clrDbtrPtyId() {
        return undrlyg.getTxInf().getOrgnlTxRef().getDbtrAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String clrDbtrWltId() {
        return null;
    }

    @Override
    public String clrDbtrSysId() {
        return null;
    }

    @Override
    public String clrCdtrPtyId() {
        return undrlyg.getTxInf().getOrgnlTxRef().getCdtrAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String clrCdtrWltId() {
        return null;
    }

    @Override
    public String clrCdtrSysId() {
        return undrlyg.getTxInf().getOrgnlTxRef().getCdtrAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String clrCurrency() {
        return undrlyg.getTxInf().getOrgnlTxRef().getIntrBkSttlmAmt().getCcy();
    }

    @Override
    public String clrAmt() {
        return undrlyg.getTxInf().getOrgnlTxRef().getIntrBkSttlmAmt().getValue();
    }

    @Override
    public String clrBizRspSts() {
        return null;
    }

    @Override
    public void clrBizRspSts(String clrBizRspSts) {

    }

    @Override
    public String clrBizRjctCd() {
        return null;
    }

    @Override
    public void clrBizRjctCd(String clrBizRjctCd) {

    }

    @Override
    public String clrRjctResn() {
        return null;
    }

    @Override
    public void clrRjctResn(String clrRjctResn) {

    }

    @Override
    public String clrTrxInf() {
        return null;
    }

    @Override
    public String clrFlag() {
        return ClrFlgEnum.NO.getCode();
    }

    @Override
    public String clrCreDtTm() {
        return assgnmt.getCreDtTm();
    }

    @Override
    public String clrSendPtyId() {
        return assgnmt.getAssgnr().getAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String clrSendSysId() {
        return assgnmt.getAssgnr().getAgt().getFinInstnId().getClrSysMmbId().getClrSysId().getCd();
    }

    @Override
    public String clrRecvPtyId() {
        return  assgnmt.getAssgne().getAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String clrRecvSysId() {
        return assgnmt.getAssgne().getAgt().getFinInstnId().getClrSysMmbId().getClrSysId().getCd();
    }

    @Override
    public String clrPresumeTm() {
        return null;
    }

    @Override
    public void fillBatchId(Response<ClearingStatus> response) {

    }

    @Override
    public String clrAcctTp() {
        return null;
    }

    @Override
    public String encode() {
        return null;
    }

    @Override
    public RecordDTO decode(String encode) {
        return null;
    }

    @Override
    public String recMsgId() {
        return assgnmt.getId();
    }

    @Override
    public String recMsgTp() {
        return MsgTpEnum.REVERSE_REQUREST.getCode();
    }

    @Override
    public String recOrgnlMsgId() {
        return undrlyg.getTxInf().getOrgnlGrpInf().getOrgnlMsgId();
    }

    @Override
    public String recOrgnlMsgTp() {
        return undrlyg.getTxInf().getOrgnlGrpInf().getOrgnlMsgNmId();
    }


}
