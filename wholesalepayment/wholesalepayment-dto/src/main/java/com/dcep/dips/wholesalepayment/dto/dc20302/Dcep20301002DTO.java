/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc20302;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.annotation.GwMethod;
import com.dcep.common.annotation.RpcInfo;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.ValidateUtils;
import com.dcep.common.validator.Priority;
import com.dcep.dips.common.constant.ChnlSysEnum;
import com.dcep.dips.common.util.DateUtils;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.constants.Constant;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.DtoCheckUtil;
import com.dcep.dips.wholesalepayment.validation.CheckClrBatId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * 付款请求报文dcep.203.001.02
 * 
 * @author cxf
 * @version $Id: Dcep20301002.java
 */
@JacksonXmlRootElement(localName = "FIToFICstmrCdtTrf", namespace = "http://www.dcep.com/dcep/20301002/")
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Gateway(msgTp = "dcep.203.010.02", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {
        @RpcInfo(name = Constant.NAME_CHAIN, methods = { @GwMethod(name = Constant.METHOD_UPCHAIN) }) }))
@Clearing(presumeStatus = ClearingStatusEnum.NONE, action = ClearingActionEnum.PREPARE, confirmTimeout = 300, instgDrctPty = InstgDrctPtyEnum.DBTR, checkMode = ClearingCheckModeEnum.PAYER_CHECK, cdtDbtInd = ClearingProdCdtDbtIndEnum.CRDT)
@Record(saveMode = RecordSaveModeEnum.ALL)
@CheckClrBatId
public class Dcep20301002DTO extends GwDTO implements ClearingDTO, DataEncryption {

    /**  */
    private static final long serialVersionUID = -7274666702072271932L;

    /**
     * 【业务头组件】
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private GrpHdr grpHdr;

    /**
     * CreditTransferTransactionInformation
     */
    @JacksonXmlProperty(localName = "CdtTrfTxInf")
    @NotNull
    @Valid
    private CdtTrfTxInf cdtTrfTxInf;

    @Override
    public String recMsgTp() {
        return MsgTpEnum.CDT_REQUEST_NMTBN.getCode();
    }

    @Override
    public String recMsgId() {
        return grpHdr.getMsgId();
    }

//    @Override
//    public String recDbtrWltId() {
//        return cdtTrfTxInf.getDbtrAcct().getId().getOthr().getId();
//    }
//
//    @Override
//    public String recCdtrWltId() {
//        return cdtTrfTxInf.getCdtrAcct() != null ? cdtTrfTxInf.getCdtrAcct().getId().getOthr().getId() : null;
//    }

    @Override
    public String recOrgnlMsgTp() {
        return null;
    }

    @Override
    public String recOrgnlMsgId() {
        return null;
    }

    @Override
    public String clrMsgTp() {

        return MsgTpEnum.CDT_REQUEST_NMTBN.getCode();
    }

    @Override
    public String clrEndToEndId() {
        return cdtTrfTxInf.getPmtId().getEndToEndId();
    }

    @Override
    public String getClrMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public String clrBizTp() {
        return cdtTrfTxInf.getPmtTpInf().getCtgyPurp().getPrtry();
    }

    @Override
    public String clrBizKind() {
        return cdtTrfTxInf.getPurp().getPrtry();
    }

    @Override
    public String clrDbtrPtyId() {
        return cdtTrfTxInf.getInstgAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String clrDbtrWltId() {
        return cdtTrfTxInf.getDbtrAcct().getId().getOthr().getId();
    }

    @Override
    public String clrDbtrSysId() {
        return ChnlSysEnum.DCEP.getCode();
    }

    @Override
    public String clrCdtrPtyId() {
        return cdtTrfTxInf.getInstdAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String clrCdtrWltId() {
        return cdtTrfTxInf.getCdtrAcct().getId().getOthr().getId();
    }

    @Override
    public String clrCdtrSysId() {
        return ChnlSysEnum.BCSP.getCode();
    }

    @Override
    public String clrCurrency() {
        return cdtTrfTxInf.getIntrBkSttlmAmt().getCcy();
    }

    @Override
    public String clrAmt() {
        //TODO 转成分返回？
        return cdtTrfTxInf.getIntrBkSttlmAmt().getValue();
    }

    @Override
    public String clrBizRspSts() {
        return null;
    }

    @Override
    public String clrBizRjctCd() {
        return null;
    }

    @Override
    public String clrRjctResn() {
        return null;
    }

    @Override
    public String clrTrxInf() {
        // 交易描述信息(付款报文,交易描述信息为收款人钱包ID)
        return cdtTrfTxInf.getCdtrAcct() != null ? cdtTrfTxInf.getCdtrAcct().getId().getOthr().getId() : null;
    }

    @Override
    public String clrFlag() {
        return ClrFlgEnum.YES.getCode();
    }

    /**
     * 将ustrds附言字段的值赋值到RmtInf对象
     *
     * @param ustrds
     */
    void rmtInf(List<String> ustrds) {
        if (ustrds != null && !ustrds.isEmpty()) {
            this.getCdtTrfTxInf().setRmtInf(new RmtInf(ustrds));
        }
    }

    /**
     * @see GwDTO#init()
     */
    @Override
    public void init() {
        rmtInf(this.getCdtTrfTxInf().getUstrds());
    }

    /**
     * @see GwDTO#fetchMsgId()
     */
    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public void fillBatchId(Response<ClearingStatus> response) {
    }

    public OrgnlGrpHdr presumeConfirm() {

        OrgnlGrpHdr orgnlGrpHdr = new OrgnlGrpHdr();
        orgnlGrpHdr.setOrgnlInstgPty(this.cdtTrfTxInf.getInstgAgt().getFinInstnId().getClrSysMmbId().getMmbId());
        orgnlGrpHdr.setOrgnlMsgId(this.getGrpHdr().getMsgId());
        orgnlGrpHdr.setOrgnlMT(MsgTpEnum.CDT_REQUEST_NMTBN.getCode());
        return orgnlGrpHdr;

    }

//    @Override
//    public void fillPlatPrcSts() {
//
//    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        // 报文头报文标识号
        String headerMsgId = soapHeader.getMsgSN().substring(0, 32);
        // 报文体报文标识号
        String bodyMsgId = grpHdr.getMsgId();
        if (!headerMsgId.equals(bodyMsgId)) {
            throw new DcepException(ErrorEnum.MSGSN_MSGID_NOT_MATCH_ERROR);
        }
        // 报文标识号与明细标识号校验
        if (!headerMsgId.equals(cdtTrfTxInf.getPmtId().getTxId())) {
            throw new DcepException(ErrorEnum.MSGID_TXID_NOT_MATCH_ERROR);
        }

        // 校验msgId的(13,16)这3位与msgTp的中间3位是否一致 报文编号校验11
        String bodyMsgIdMsgTp = bodyMsgId.substring(12, 15);
        String msgTpNum = soapHeader.getMsgTp().substring(5, 8);
        if (!msgTpNum.equals(bodyMsgIdMsgTp)) {
            throw new DcepException(ErrorEnum.MSGTP_NOT_IN_MSGID_ERROR);
        }
        
        // 航运链渠道信息必填
//        RmtInf rmtInf = this.getCdtTrfTxInf().getRmtInf();
//        if (null == rmtInf
//            || StringUtils.isBlank(rmtInf.getSndChnlSys())
//            || StringUtils.isBlank(rmtInf.getRcvChnlSys())) {
//            throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "航运链渠道信息必填");
//        }

//        if (rmtInf.getSndChnlSys().equals(rmtInf.getRcvChnlSys())) {
//            throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "发起接收渠道系统标识非法");
//        }

        // 校验附言Ustrds里标签字段
        ValidateUtils.validate(this.getCdtTrfTxInf().getRmtInf());

        return DtoCheckUtil.checkPayerMsgInst(soapHeader.getSender(), this.clrDbtrPtyId(), soapHeader.getReceiver(),
                this.clrCdtrPtyId());

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
    public void clrBatId(String clrBatId) {
        cdtTrfTxInf.getPmtId().setInstrId(clrBatId);
    }

    @Override
    public String clrBatId() {
        return cdtTrfTxInf.getPmtId().getInstrId();
    }

    @Override
    public String clrTransTp() {
        //TODO settle trans_tp 交易类型 上链报文没找到对应
        return TransTpEnum.NORMAL_TRANS.getCode();
    }

    @Override
    public void clrBizRspSts(String clrBizRspSts) {
        // 业务回执状态
    }

    @Override
    public void clrBizRjctCd(String clrBizRjctCd) {
        // 业务拒绝码
    }

    @Override
    public void clrRjctResn(String clrRjctResn) {
        // 业务拒绝原因
    }

    @Override
    public String clrCreDtTm() {
        return grpHdr.getCreDtTm();
    }

    @Override
    public String clrSendPtyId() {
        //上链业务，发起机构是付款运营机构
        return cdtTrfTxInf.getInstgAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String clrSendSysId() {
        return ChnlSysEnum.DCEP.getCode();
    }

    @Override
    public String clrRecvPtyId() {
        //上链业务，接收机构是收款款运营机构
        return cdtTrfTxInf.getInstdAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String clrRecvSysId() {
        return ChnlSysEnum.BCSP.getCode();
    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        List<String> encryptList = encryptionHelper.encrypt(fetchEncryptionFeatures());
        encryptionFeaturesAssign(encryptList);
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        List<String> decryptList = encryptionHelper.decrypt(fetchEncryptionFeatures());
        encryptionFeaturesAssign(decryptList);
    }

//    @Override
//    public String clrSendInst() {
//        return cdtTrfTxInf.getInstgAgt().getFinInstnId().getClrSysMmbId().getMmbId();
//    }

    @Override
    public String clrPresumeTm() {
        return null;
    }

    @Override
    public String clrAcctTp() {
        return ClrAcctTpEnum.CHAIN_UP.getCode();
    }

//    @Override
//    public String cdtrAcctPrxyId(){
//        return cdtTrfTxInf.getCdtrAcct().getPrxy().getId();
//    }


    @Override
    public String clrSndChnlSys() {
        // 发起渠道
        if (cdtTrfTxInf.getRmtInf() != null) {
            return cdtTrfTxInf.getRmtInf().getSndChnlSys();
        }
        return null;
    }

    @Override
    public String clrRcvChnlSys() {
        // 接收渠道
        if (cdtTrfTxInf.getRmtInf() != null) {
            return cdtTrfTxInf.getRmtInf().getRcvChnlSys();
        }
        return null;
    }

    public List<String> fetchEncryptionFeatures() {
        // 获取需加解密处理的敏感要素，若为空则赋值为null，保证前后顺序一致
        List<String> data = new ArrayList<>();
        if (cdtTrfTxInf != null && cdtTrfTxInf.getDbtr() != null && cdtTrfTxInf.getDbtr().getNm() != null) {
            data.add(cdtTrfTxInf.getDbtr().getNm());
        }

        if (cdtTrfTxInf != null && cdtTrfTxInf.getDbtrAcct() != null
                && cdtTrfTxInf.getDbtrAcct().getId() != null
                && cdtTrfTxInf.getDbtrAcct().getId().getOthr() != null
                && cdtTrfTxInf.getDbtrAcct().getId().getOthr().getId() != null) {
            data.add(cdtTrfTxInf.getDbtrAcct().getId().getOthr().getId());
        }

        if (cdtTrfTxInf != null && cdtTrfTxInf.getCdtr() != null && cdtTrfTxInf.getCdtr().getNm() != null) {
            data.add(cdtTrfTxInf.getCdtr().getNm());
        }

        if (cdtTrfTxInf != null && cdtTrfTxInf.getCdtrAcct() != null
                && cdtTrfTxInf.getCdtrAcct().getId() != null
                && cdtTrfTxInf.getCdtrAcct().getId().getOthr() != null
                && cdtTrfTxInf.getCdtrAcct().getId().getOthr().getId() != null) {
            data.add(cdtTrfTxInf.getCdtrAcct().getId().getOthr().getId());
        }

        return data;
    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        // 按顺序赋值加解密处理后的敏感要素
        if (cdtTrfTxInf != null && cdtTrfTxInf.getDbtr() != null && cdtTrfTxInf.getDbtr().getNm() != null) {
            cdtTrfTxInf.getDbtr().setNm(encryptionFeatures.get(index++));
        }

        if (cdtTrfTxInf != null && cdtTrfTxInf.getDbtrAcct() != null
                && cdtTrfTxInf.getDbtrAcct().getId() != null
                && cdtTrfTxInf.getDbtrAcct().getId().getOthr() != null
                && cdtTrfTxInf.getDbtrAcct().getId().getOthr().getId() != null) {
            cdtTrfTxInf.getDbtrAcct().getId().getOthr().setId(encryptionFeatures.get(index++));
        }

        if (cdtTrfTxInf != null && cdtTrfTxInf.getCdtr() != null && cdtTrfTxInf.getCdtr().getNm() != null) {
            cdtTrfTxInf.getCdtr().setNm(encryptionFeatures.get(index++));
        }

        if (cdtTrfTxInf != null && cdtTrfTxInf.getCdtrAcct() != null
                && cdtTrfTxInf.getCdtrAcct().getId() != null
                && cdtTrfTxInf.getCdtrAcct().getId().getOthr() != null
                && cdtTrfTxInf.getCdtrAcct().getId().getOthr().getId() != null) {
            cdtTrfTxInf.getCdtrAcct().getId().getOthr().setId(encryptionFeatures.get(index++));
        }
    }
    public String clrSysWorkDt(){
        //TODO 系统工作日期？？
        return DateUtils.today();
    }
}
