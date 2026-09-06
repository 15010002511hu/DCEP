/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc202;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.ValidateUtils;
import com.dcep.common.validator.Priority;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.infocache.manager.NacosConsume;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@JacksonXmlRootElement(localName = "FIToFIPmtStsRpt", namespace = "http://www.dcep.com/dcep/20201001/")
@Setter
@Getter
@ToString(callSuper = true)
@Gateway(msgTp = "dcep.202.010.01", isReturn = true)
@Record(saveMode = RecordSaveModeEnum.ALL)
public class Dcep20201001DTO extends GwDTO implements ClearingDTO, DataEncryption {

	/**  */
	private static final long serialVersionUID = -7302235204608921494L;
	/**
	 * 【业务头组件】
	 */
	@JacksonXmlProperty(localName = "GrpHdr")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private GrpHdr grpHdr;

	/**
	 * 【原业务头组件】
	 */
	@JacksonXmlProperty(localName = "OrgnlGrpInfAndSts")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private OrgnlGrpInfAndSts orgnlGrpInfAndSts;

	/**
	 * TransactionInformationAndStatus
	 */
	@JacksonXmlProperty(localName = "TxInfAndSts")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private TxInfAndSts txInfAndSts;

	/**
	 * 将ustrds附言字段的值赋值到RmtInf对象
	 * 
	 * @param ustrds
	 */
	void rmtInf(List<String> ustrds) {
		if (ustrds != null && !ustrds.isEmpty()) {
			this.getTxInfAndSts().getOrgnlTxRef().setRmtInf(new RmtInf(ustrds));
		}
	}

	/**
	 * @see com.dcep.common.model.GwDTO#init()
	 */
	@Override
	public void init() {
		rmtInf(this.txInfAndSts.getOrgnlTxRef().getUstrds());
	}

	/**
	 * @see com.dcep.common.model.GwDTO#fetchMsgId()
	 */
	@Override
	public String fetchMsgId() {
		return grpHdr.getMsgId();
	}

	@Override
	public void fillBatchId(Response<ClearingStatus> response) {
		// 向报文中赋交易批次号
		txInfAndSts.setOrnlInstrId(response.getResult().getBatchId());
	}

	@Override
	public void fillPlatPrcSts(String bizSts) {
		// 向报文中赋业务状态(平台填写)
		if (orgnlGrpInfAndSts.getStsRsnInf() == null) {
			StsRsnInf stsRsnInf = new StsRsnInf();
			stsRsnInf.setAddtlInf(bizSts);
			orgnlGrpInfAndSts.setStsRsnInf(stsRsnInf);
		} else {
			orgnlGrpInfAndSts.getStsRsnInf().setAddtlInf(bizSts);
		}
	}

	@Override
	public boolean check(SoapHeader soapHeader) {
		// 原请求报文标识号
		String headerMsgId = soapHeader.getMsgSN().substring(0, 32);
		// 原请求报文标识号
		String bodyMsgId = orgnlGrpInfAndSts.getOrgnlMsgId();
		if (!headerMsgId.equals(bodyMsgId)) {
			throw new DcepException(ErrorEnum.MSGSN_MSGID_NOT_MATCH_ERROR);
		}

		// 校验附言Ustrds里标签字段
		RmtInf rmtInf = this.getTxInfAndSts().getOrgnlTxRef().getRmtInf();
		ValidateUtils.validate(rmtInf);
		
        if (NacosConsume.getCredttmInterval().contains(MsgTpEnum.CDT_RESPONSE_ABBR.getCode())
                && ClearingStatusEnum.SUCCESS.getCode().equals(this.clrBizRspSts())) {
            // 用户居民类型必填
            if (rmtInf == null || (rmtInf != null && StringUtils.isBlank(rmtInf.getResdtTp()))) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "收款用户居民类型必填");
            }
            // 用户常驻国家/地区代码必填
            if (rmtInf == null
                    || (rmtInf != null && StringUtils.isBlank(rmtInf.getResdtCtryCd()))) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "收款用户常驻国家/地区代码必填");
            }
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
	public String recMsgTp() {
		return MsgTpEnum.CDT_RESPONSE.getCode();
	}

	@Override
	public String recMsgId() {
		return orgnlGrpInfAndSts.getOrgnlMsgId();
	}

	@Override
	public String recOrgnlMsgTp() {
		return orgnlGrpInfAndSts.getOrgnlMsgNmId();
	}

	@Override
	public String recOrgnlMsgId() {
		return orgnlGrpInfAndSts.getOrgnlMsgId();
	}

	@Override
	public String clrMsgTp() {
		return MsgTpEnum.CDT_RESPONSE.getCode();
	}

	@Override
	public String clrEndToEndId() {
		return null;
	}

	@Override
	public String getClrMsgId() {
		return orgnlGrpInfAndSts.getOrgnlMsgId();
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
		return txInfAndSts.getOrgnlTxRef().getDbtrAgt().getFinInstnId().getClrSysMmbId().getMmbId();
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
		return txInfAndSts.getOrgnlTxRef().getCdtrAgt().getFinInstnId().getClrSysMmbId().getMmbId();
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
	public String clrCurrency() {
		return txInfAndSts.getOrgnlTxRef().getIntrBkSttlmAmt().getCcy();
	}

	@Override
	public String clrAmt() {
		return txInfAndSts.getOrgnlTxRef().getIntrBkSttlmAmt().getValue();
	}

	@Override
	public String clrBizRspSts() {
		return txInfAndSts.getStsId();
	}

	@Override
	public String clrBizRjctCd() {
		if (txInfAndSts.getStsRsnInf() != null && txInfAndSts.getStsRsnInf().getRsn() != null) {
			return txInfAndSts.getStsRsnInf().getRsn().getPrtry();
		}
		return null;
	}

	@Override
	public String clrRjctResn() {
		if (txInfAndSts.getStsRsnInf() != null) {
			return txInfAndSts.getStsRsnInf().getAddtlInf();
		}
		return null;
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
		txInfAndSts.setOrnlInstrId(clrBatId);
	}

	@Override
	public String clrBatId() {
		return txInfAndSts.getOrnlInstrId();
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
	public String fetchResultCode() {
		if (txInfAndSts.getStsId() != null) {
			return txInfAndSts.getStsId() + "-"
					+ (ClearingStatusEnum.SUCCESS.getCode().equals(txInfAndSts.getStsId())
							? ClearingPrcCdEnum.BUSI_SUCCESS.getCode()
							: ClearingPrcCdEnum.BUSI_REJT.getCode());
		}
		return "";
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
	public String clrPresumeTm() {
		return null;
	}


	@Override
	public String clrAcctTp() {
		return ClrAcctTpEnum.PAY.getCode();
	}

	@Override
	public String clrCdtrNm() {
		if (null != txInfAndSts && null != txInfAndSts.getOrgnlTxRef()
				&& null != txInfAndSts.getOrgnlTxRef().getRmtInf()) {
			return txInfAndSts.getOrgnlTxRef().getRmtInf().getCdtrNm();
		} else {
			return null;
		}
	}

	@Override
	public String clrResdtTp() {
		if (null != txInfAndSts && null != txInfAndSts.getOrgnlTxRef()
			&& null != txInfAndSts.getOrgnlTxRef().getRmtInf()) {
			return txInfAndSts.getOrgnlTxRef().getRmtInf().getResdtTp();
		}
		return null;
	}

	@Override
	public String clrResdtCtryCd() {
		if (null != txInfAndSts && null != txInfAndSts.getOrgnlTxRef()
			&& null != txInfAndSts.getOrgnlTxRef().getRmtInf()) {
			return txInfAndSts.getOrgnlTxRef().getRmtInf().getResdtCtryCd();
		}
		return null;
	}

	@Override
	public String clrRegrCtryCd() {
		if (null != txInfAndSts && null != txInfAndSts.getOrgnlTxRef()
			&& null != txInfAndSts.getOrgnlTxRef().getRmtInf()) {
			return txInfAndSts.getOrgnlTxRef().getRmtInf().getRegrCtryCd();
		}
		return null;
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

	public List<String> fetchEncryptionFeatures() {
		// 获取需加解密处理的敏感要素，若为空则赋值为null，保证前后顺序一致
		List<String> data = new ArrayList<>();
		if (txInfAndSts != null && txInfAndSts.getOrgnlTxRef() != null
				&& txInfAndSts.getOrgnlTxRef().getUstrds() != null) {
			for (String ustrd : txInfAndSts.getOrgnlTxRef().getUstrds()) {
				if (ustrd.contains("/CdtrNm/")) {
					data.add(ustrd.substring("/CdtrNm/".length()));
				}
			}
		}
		
		return data;
	}

	public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
		// 按顺序赋值加解密处理后的敏感要素
		if (txInfAndSts != null && txInfAndSts.getOrgnlTxRef() != null
				&& txInfAndSts.getOrgnlTxRef().getUstrds() != null) {
			for (String ustrd : txInfAndSts.getOrgnlTxRef().getUstrds()) {
				if (ustrd.contains("/CdtrNm/")) {
					txInfAndSts.getOrgnlTxRef().getUstrds()
							.set(txInfAndSts.getOrgnlTxRef().getUstrds().indexOf(ustrd), 
									"/CdtrNm/" + encryptionFeatures.get(0));
				}
			}
		}
	}
}
