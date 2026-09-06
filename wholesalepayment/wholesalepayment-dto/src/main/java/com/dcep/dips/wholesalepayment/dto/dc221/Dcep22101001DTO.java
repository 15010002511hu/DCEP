/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc221;

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
import com.dcep.dips.wholesalepayment.utils.DtoCheckUtil;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.infocache.validation.CheckClrGrpHdr;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.dubbo.common.utils.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "ReconvertReq", namespace = "http://www.dcep.com/dcep/22101001/")
@Setter
@Getter
@ToString
@Gateway(msgTp = "dcep.221.010.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {
		@RpcInfo(name = Constant.NAME_PAYMENT, methods = { @GwMethod(name = Constant.DBTR_SETTLE) }) }))
@Clearing(presumeStatus = ClearingStatusEnum.PRESUME_SUCCESS, action = ClearingActionEnum.PREPARE, instgDrctPty = InstgDrctPtyEnum.DBTR, checkMode = ClearingCheckModeEnum.PAYER_CHECK, cdtDbtInd = ClearingProdCdtDbtIndEnum.CRDT)
@Record(saveMode = RecordSaveModeEnum.ALL)
@CheckBizCode(bizTypeCode = "trxInf.trxBizTp", bizCtgyCode = "trxInf.trxCtgyPurpCd")
@CheckAccountTag(type = Type.WID, path = {"dbtrInf.dbtrWltId"})
public class Dcep22101001DTO extends GwDTO implements ClearingDTO, DataEncryption {

	/**  */
	private static final long serialVersionUID = 5607804799958894873L;

	/**
	 * 【业务头组件】
	 */
	@JacksonXmlProperty(localName = "GrpHdr")
	@NotNull(groups = Priority.Highest.class)
	@CheckGrpHdrMsgId(groups = Priority.Lowest.class)
	@CheckClrGrpHdr(groups = Priority.Lowest.class)
	@Valid
	private GrpHdr grpHdr;

	/**
	 * 【交易信息】
	 */
	@JacksonXmlProperty(localName = "TrxInf")
	@NotNull
	@Valid
	private TrxInf trxInf;

	/**
	 * 【兑回钱包信息】
	 */
	@JacksonXmlProperty(localName = "DbtrInf")
	@NotNull
	@Valid
	private DbtrInf dbtrInf;

	/**
	 * 【收款人信息】
	 */
	@JacksonXmlProperty(localName = "CdtrInf")
	@NotNull
	@Valid
	private CdtrInf cdtrInf;

	/**
	 * 【钱柜机构信息】
	 */
	@JacksonXmlProperty(localName = "CshBoxInf")
	@Valid
	private CshBoxInf cshBoxInf;
	
	/**
     * 【原兑出交易信息】
     */
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    @Valid
    private OrgnlGrpHdr orgnlGrpHdr;

	@Override
	public void init() {

	}

	/**
	 * @see GwDTO#fetchMsgId()
	 */
	@Override
	public String fetchMsgId() {
		return grpHdr.getMsgId();
	}

	@Override
	public OrgnlGrpHdr presumeConfirm() {

		OrgnlGrpHdr orgnlGrpHdr = new OrgnlGrpHdr();
		orgnlGrpHdr.setOrgnlInstgPty(this.getGrpHdr().getInstgPty().getInstgDrctPty());
		orgnlGrpHdr.setOrgnlMsgId(this.getGrpHdr().getMsgId());
		orgnlGrpHdr.setOrgnlMT(MsgTpEnum.RECOV_REQUEST.getCode());
		return orgnlGrpHdr;
	}


	@Override
	public boolean check(SoapHeader soapHeader) {
		if ("WT09".equals(dbtrInf.getDbtrWltTp())) {
			if (StringUtils.isBlank(trxInf.getTrxFndSrc())) {
				throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当付款人钱包类型为对公钱包时，交易资金来源必填");
			}
		}

		if ((Common.PURP_RECON.equals(trxInf.getTrxBizTp())
			|| Common.PURP_RECON_TP1.equals(trxInf.getTrxCtgyPurpCd())
			|| Common.PURP_RECON_TP2.equals(trxInf.getTrxCtgyPurpCd())
			|| Common.PURP_RECON_TP3.equals(trxInf.getTrxCtgyPurpCd())
			|| Common.PURP_RECON_TP4.equals(trxInf.getTrxCtgyPurpCd()))
			&& StringUtils.isBlank(dbtrInf.getDbtrNm())) {
			throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当业务类型为兑回时，付款人名称信息必填");
		}

		if ((Common.PURP_CONREF_RECON.equals(trxInf.getTrxBizTp())
			|| Common.PURP_RED_RECON_TP5.equals(trxInf.getTrxCtgyPurpCd())
			|| Common.PURP_RED_RECON_TP6.equals(trxInf.getTrxCtgyPurpCd())
			|| Common.PURP_RED_RECON_TP7.equals(trxInf.getTrxCtgyPurpCd())
			|| Common.PURP_RED_RECON_TP8.equals(trxInf.getTrxCtgyPurpCd()))
			&& orgnlGrpHdr == null) {
			throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当业务类型为退款兑回时，原兑出交易信息必填");
		}
		
		return !CheckUtils.requestMsgChk(soapHeader, grpHdr) ? false
				: DtoCheckUtil.checkPayerMsgInst(soapHeader.getSender(), this.clrDbtrPtyId(), soapHeader.getReceiver(),
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
	public String recMsgTp() {
		return MsgTpEnum.RECOV_REQUEST.getCode();
	}

	@Override
	public String recMsgId() {
		return grpHdr.getMsgId();
	}

	@Override
	public String clrDbtrWltId() {
		return dbtrInf.getDbtrWltId();
	}

	@Override
	public String clrDbtrSysId() {
		return ChnlSysEnum.DCEP.getCode();
	}

	@Override
	public String clrCdtrWltId() {
		return cdtrInf.getCdtrAcct();
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
		return MsgTpEnum.RECOV_REQUEST.getCode();
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
		// 交易描述信息(兑回报文时为收款人名称-收款人账户账号)
		return cdtrInf.getCdtrNm() + "-" + cdtrInf.getCdtrAcct();
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
		// 向报文中赋交易批次号
		trxInf.setBatchId(response.getResult().getBatchId());
	}


	@Override
	public String clrAcctTp() {
		return InfoCacheUtil.checkInstType(this.clrCdtrPtyId()) ? ClrAcctTpEnum.PAY.getCode()
				: ClrAcctTpEnum.CASH_IN.getCode();
	}

    public List<String> fetchEncryptionFeatures() {
        // 获取需加解密处理的敏感要素，若为空则赋值为null，保证前后顺序一致
        List<String> data = new ArrayList<>();
	    if (dbtrInf != null && null != dbtrInf.getDbtrNm()) {
		    data.add(dbtrInf.getDbtrNm());
	    }

	    if (dbtrInf != null && null != dbtrInf.getDbtrWltId()) {
		    data.add(dbtrInf.getDbtrWltId());
	    }

	    if (cdtrInf != null && null != cdtrInf.getCdtrNm()) {
		    data.add(cdtrInf.getCdtrNm());
	    }
	    if (cdtrInf != null && null != cdtrInf.getCdtrAcct()) {
		    data.add(cdtrInf.getCdtrAcct());
	    }
        return data;
    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
		int index = 0;
        // 按顺序赋值加解密处理后的敏感要素
	    if (dbtrInf != null && null != dbtrInf.getDbtrNm()) {
		    dbtrInf.setDbtrNm(encryptionFeatures.get(index++));
	    }

	    if (dbtrInf != null && null != dbtrInf.getDbtrWltId()) {
		    dbtrInf.setDbtrWltId(encryptionFeatures.get(index++));
	    }

	    if (cdtrInf != null && null != cdtrInf.getCdtrNm()) {
		    cdtrInf.setCdtrNm(encryptionFeatures.get(index++));
	    }
	    if (cdtrInf != null && null != cdtrInf.getCdtrAcct()) {
		    cdtrInf.setCdtrAcct(encryptionFeatures.get(index++));
	    }
    }

	@Override
	public String clrSysWorkDt(){
		return trxInf.getSysWorkDt();
	}

	@Override
	public void fillSttlmDt(String sttlmDt) {
		trxInf.setSttlmDt(sttlmDt);
	}
}
