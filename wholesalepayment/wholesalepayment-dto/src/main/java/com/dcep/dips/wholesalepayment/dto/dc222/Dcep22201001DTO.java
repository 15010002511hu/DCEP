/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc222;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.infocache.validation.CheckClrGrpHdr;
import com.dcep.infocache.validation.CheckClrOrgnlGrpHdr;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JacksonXmlRootElement(localName = "ReconvertRsp", namespace = "http://www.dcep.com/dcep/22201001/")
@Setter
@Getter
@ToString
@Gateway(msgTp = "dcep.222.010.01", isReturn = true)
@Record(saveMode = RecordSaveModeEnum.ALL)
public class Dcep22201001DTO extends GwDTO implements ClearingDTO {

	/**  */
	private static final long serialVersionUID = 4384316990011927276L;

	/**
	 * 【业务头组件】
	 */
	@JacksonXmlProperty(localName = "GrpHdr")
	@NotNull
	@Valid
	@CheckGrpHdrMsgId
	@CheckClrGrpHdr
	private GrpHdr grpHdr;

	/**
	 * 【原业务头组件】
	 */
	@JacksonXmlProperty(localName = "OrgnlGrpHdr")
	@NotNull
	@CheckClrOrgnlGrpHdr
	@Valid
	private OrgnlGrpHdr orgnlGrpHdr;

	/**
	 * ResponsionInformation
	 */
	@JacksonXmlProperty(localName = "RspsnInf")
	@NotNull
	@Valid
	private RspsnInf rspsnInf;

	/**
	 * @see GwDTO#init()
	 */
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
	public void fillBatchId(Response<ClearingStatus> response) {
		if (response != null) {
			rspsnInf.setBatchId(response.getResult().getBatchId());
		}
	}

	@Override
	public void fillPlatPrcSts(String bizSts) {
		// 业务状态(平台赋值)
		rspsnInf.setPrcSts(bizSts);
	}

	@Override
	public boolean check(SoapHeader soapHeader) {
		return CheckUtils.responseMsgChk(soapHeader, grpHdr, orgnlGrpHdr);
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
		return MsgTpEnum.RECOV_RESPONSE.getCode();
	}

	@Override
	public String recMsgId() {
		return orgnlGrpHdr.getOrgnlMsgId();
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
	public String clrCdtrWltId() {
		return null;
	}

	@Override
	public String clrCdtrSysId() {
		return null;
	}

	@Override
	public String recOrgnlMsgTp() {
		return orgnlGrpHdr.getOrgnlMT();
	}

	@Override
	public String recOrgnlMsgId() {
		return orgnlGrpHdr.getOrgnlMsgId();
	}

	@Override
	public String clrMsgTp() {
		return MsgTpEnum.RECOV_RESPONSE.getCode();
	}

	@Override
	public String clrEndToEndId() {
		return null;
	}

	@Override
	public String getClrMsgId() {
		return orgnlGrpHdr.getOrgnlMsgId();
	}

	@Override
	public String clrBizTp() {
		return null;
	}

	@Override
	public String clrBizKind() {
		return null;
	}

	@Override
	public String clrDbtrPtyId() {
		return grpHdr.getInstdPty().getInstdDrctPty();
	}

	@Override
	public String clrCdtrPtyId() {
		return grpHdr.getInstgPty().getInstgDrctPty();
	}

	@Override
	public String clrCurrency() {
		return null;
	}

	@Override
	public String clrAmt() {
		return null;
	}

	@Override
	public String clrBizRspSts() {
		return rspsnInf.getRspsnSts();
	}

	@Override
	public String clrBizRjctCd() {
		return rspsnInf.getRjctCd();
	}

	@Override
	public String clrRjctResn() {
		return rspsnInf.getRjctInf();
	}

	@Override
	public String clrTrxInf() {
		return null;
	}

	@Override
	public String clrFlag() {
		return null;
	}

	@Override
	public void clrBatId(String clrBatId) {
		rspsnInf.setBatchId(clrBatId);
	}

	@Override
	public String clrBatId() {
		return rspsnInf.getBatchId();
	}

	@Override
	public String clrTransTp() {
		return null;
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
		return null;
	}

	@Override
	public String clrSendSysId() {
		return null;
	}

	@Override
	public String clrRecvPtyId() {
		return null;
	}

	@Override
	public String clrRecvSysId() {
		return null;
	}

	@Override
	public String fetchResultCode() {
		if (rspsnInf.getRspsnSts() != null) {
			return rspsnInf.getRspsnSts() + "-"
					+ (ClearingStatusEnum.SUCCESS.getCode().equals(rspsnInf.getRspsnSts())
							? ClearingPrcCdEnum.BUSI_SUCCESS.getCode()
							: ClearingPrcCdEnum.BUSI_REJT.getCode());
		}
		return "";
	}

	@Override
	public OrgnlGrpHdr presumeConfirm() {
		return null;
	}

	@Override
	public String clrPresumeTm() {
		return null;
	}

	@Override
	public String clrAcctTp() {
		return InfoCacheUtil.checkInstType(this.clrCdtrPtyId()) ? ClrAcctTpEnum.PAY.getCode()
				: ClrAcctTpEnum.CASH_IN.getCode();
	}

}
