/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc225;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.*;
import com.dcep.common.annotation.CheckAccountTag.Type;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.dips.common.constant.ChnlSysEnum;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.constants.Constant;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.CshBoxInf;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.dto.common.Common;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.infocache.validation.CheckClrGrpHdr;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "ConvertReq", namespace = "http://www.dcep.com/dcep/22501001/")
@Setter
@Getter
@ToString
@Gateway(msgTp = "dcep.225.010.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {
        @RpcInfo(name = Constant.NAME_PAYMENT, methods = { @GwMethod(name = Constant.CDTR_SETTLE) }) }))
@Clearing(presumeStatus = ClearingStatusEnum.PRESUME_FAILED, action = ClearingActionEnum.PREPARE, instgDrctPty = InstgDrctPtyEnum.CDTR, checkMode = ClearingCheckModeEnum.PAYEE_CHECK, cdtDbtInd = ClearingProdCdtDbtIndEnum.DBIT)
@Record(saveMode = RecordSaveModeEnum.ALL)
@CheckBizCode(bizTypeCode = "trxInf.trxBizTp", bizCtgyCode = "trxInf.trxCtgyPurpCd")
@CheckAccountTag(type = Type.WID, path = {"cdtrInf.cdtrWltId"})
@CheckAccountTag(type = Type.WID, path = {"cshBoxInf.coopBankWltId"})
public class Dcep22501001DTO extends GwDTO implements ClearingDTO, DataEncryption {
    /**  */
    private static final long serialVersionUID = 2141127244834465116L;

    /**
     * 【交易信息】
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @CheckClrGrpHdr(groups = Priority.Lowest.class)
    private GrpHdr grpHdr;

    /**
     * 【交易信息】
     */
    @JacksonXmlProperty(localName = "TrxInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private TrxInf trxInf;

    /**
     * 【付款账户信息】
     */
    @JacksonXmlProperty(localName = "DbtrInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private DbtrInf dbtrInf;

    /**
     * 【兑回钱包信息】
     */
    @JacksonXmlProperty(localName = "CdtrInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private CdtrInf cdtrInf;

    /**
     * 【钱柜机构信息】
     */
    @JacksonXmlProperty(localName = "CshBoxInf")
    @Valid
    private CshBoxInf cshBoxInf;
    
    /**
     * 【订单信息】
     */
    @JacksonXmlProperty(localName = "OrdrInf")
    @Valid
    private OrdrInf ordrInf;

    @Override
    public String encode() {
        return JSON.toJSONString(this);
    }

    @Override
    public RecordDTO decode(String encode) {
        return JSONObject.toJavaObject(JSON.parseObject(encode), this.getClass());
    }

    @Override
    public OrgnlGrpHdr presumeConfirm() {
        OrgnlGrpHdr orgnlGrpHdr = new OrgnlGrpHdr();
        orgnlGrpHdr.setOrgnlInstgPty(this.getGrpHdr().getInstgPty().getInstgDrctPty());
        orgnlGrpHdr.setOrgnlMsgId(this.getGrpHdr().getMsgId());
        orgnlGrpHdr.setOrgnlMT(MsgTpEnum.COV_REQUREST.getCode());
        return orgnlGrpHdr;
    }


    @Override
    public String recMsgTp() {
        return MsgTpEnum.COV_REQUREST.getCode();
    }

    @Override
    public String recMsgId() {

        return grpHdr.getMsgId();
    }

    @Override
    public String clrDbtrWltId() {
    	return dbtrInf.getDbtrAcct();
    }

    @Override
    public String clrDbtrSysId() {
        return ChnlSysEnum.DCEP.getCode();
    }

    @Override
    public String clrCdtrWltId() {
        return cdtrInf.getCdtrWltId();
    }

    @Override
    public String clrCdtrSysId() {
        return ChnlSysEnum.DCEP.getCode();
    }

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

        return MsgTpEnum.COV_REQUREST.getCode();
    }

    @Override
    public String clrEndToEndId() {
        return null;
    }

    @Override
    public String getClrMsgId() {

        return grpHdr.getMsgId();
    }

    @Override
    public String clrBizTp() {

        return trxInf.getTrxBizTp();
    }

    @Override
    public String clrBizKind() {

        return trxInf.getTrxCtgyPurpCd();
    }

    @Override
    public String clrDbtrPtyId() {

        return dbtrInf.getDbtrPtyId();
    }

    @Override
    public String clrCdtrPtyId() {

        return cdtrInf.getCdtrPtyId();
    }

    @Override
    public String clrCurrency() {

        return trxInf.getTrxAmt().getCcy();
    }

    @Override
    public String clrAmt() {

        return trxInf.getTrxAmt().getValue();
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
        // 交易描述信息(兑出报文时为收款人钱包ID-付款人账户账号)
        return cdtrInf.getCdtrWltId() + "-" + dbtrInf.getDbtrAcct();
    }

    @Override
    public String clrFlag() {
        return ClrFlgEnum.YES.getCode();
    }

    @Override
    public void clrBatId(String clrBatId) {
        trxInf.setBatchId(clrBatId);
    }

    @Override
    public String clrBatId() {
        return trxInf.getBatchId();
    }

    @Override
    public String clrTransTp() {
        return TransTpEnum.NORMAL_TRANS.getCode();
    }

    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        if ("WT09".equals(cdtrInf.getCdtrWltTp()) && StringUtils.isBlank(trxInf.getTrxPrps())) {
            throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当收款人钱包类型为对公钱包时，交易用途必填");
        }

        if ("WL03".compareTo(cdtrInf.getCdtrWltLvl())>=0 && StringUtils.isBlank(cdtrInf.getCdtrNm())) {
            throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当收款人钱包等级为四类以下时，收款人名称必填");
        }
        
        if ((Common.PURP_CON_DBTR.equals(trxInf.getTrxBizTp())
            || Common.PURP_CONSUMER_NEW.equals(trxInf.getTrxBizTp()))
            && ordrInf == null) {
            throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当业务类型为消费D203或100时，订单信息必填");
        }
        
        return CheckUtils.requestMsgChk(soapHeader, grpHdr);
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
        return grpHdr.getInstgPty().getInstgDrctPty();
    }

    @Override
    public String clrSendSysId() {
        return ChnlSysEnum.DCEP.getCode();
    }

    @Override
    public String clrRecvPtyId() {
        return grpHdr.getInstdPty().getInstdDrctPty();
    }

    @Override
    public String clrRecvSysId() {
        return ChnlSysEnum.DCEP.getCode();
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

    @Override
    public String clrPresumeTm() {
        return null;
    }

    @Override
    public void fillBatchId(Response<ClearingStatus> response) {
        trxInf.setBatchId(response.getResult().getBatchId());
    }

    @Override
    public String clrAcctTp() {
        return InfoCacheUtil.checkInstType(this.clrDbtrPtyId()) ? ClrAcctTpEnum.PAY.getCode()
                : ClrAcctTpEnum.CASH_OUT.getCode();
    }


    public List<String> fetchEncryptionFeatures() {
        // 获取需加解密处理的敏感要素，若为空则赋值为null，保证前后顺序一致
        List<String> data = new ArrayList<>();
        if (dbtrInf != null && null != dbtrInf.getDbtrNm()) {
            data.add(dbtrInf.getDbtrNm());
        }
        if (dbtrInf != null && null != dbtrInf.getDbtrAcct()) {
            data.add(dbtrInf.getDbtrAcct());
        }

        if (cdtrInf != null && null != cdtrInf.getCdtrNm()) {
            data.add(cdtrInf.getCdtrNm());
        }
        if (cdtrInf != null && null != cdtrInf.getCdtrWltId()) {
            data.add(cdtrInf.getCdtrWltId());
        }
        return data;
    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        // 按顺序赋值加解密处理后的敏感要素
        if (dbtrInf != null && null != dbtrInf.getDbtrNm()) {
            dbtrInf.setDbtrNm(encryptionFeatures.get(index++));
        }
        if (dbtrInf != null && null != dbtrInf.getDbtrAcct()) {
            dbtrInf.setDbtrAcct(encryptionFeatures.get(index++));
        }

        if (cdtrInf != null && null != cdtrInf.getCdtrNm()) {
            cdtrInf.setCdtrNm(encryptionFeatures.get(index++));
        }
        if (cdtrInf != null && null != cdtrInf.getCdtrWltId()) {
            cdtrInf.setCdtrWltId(encryptionFeatures.get(index++));
        }
    }

    @Override
    public String clrSysWorkDt(){
        return trxInf.getSysWorkDt();
    }
}
