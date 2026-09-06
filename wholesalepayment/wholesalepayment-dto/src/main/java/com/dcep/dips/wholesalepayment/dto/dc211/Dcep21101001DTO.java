/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc211;

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
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.ValidateUtils;
import com.dcep.common.validator.Priority;
import com.dcep.dips.common.constant.ChnlSysEnum;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.constants.Constant;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.DtoCheckUtil;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@JacksonXmlRootElement(localName = "FIToFICstmrDrctDbt",namespace = "http://www.dcep.com/dcep/21101001/")
@Setter
@Getter
@ToString(callSuper = true)
@Gateway(msgTp = "dcep.211.010.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {
		@RpcInfo(name = Constant.NAME_PAYMENT, methods = { @GwMethod(name = Constant.CDTR_SETTLE) }) }))
@Clearing(presumeStatus = ClearingStatusEnum.PRESUME_FAILED, action = ClearingActionEnum.PREPARE, confirmTimeout = 30,
	checkMode = ClearingCheckModeEnum.PAYEE_CHECK, cdtDbtInd = ClearingProdCdtDbtIndEnum.DBIT)
@Record(saveMode = RecordSaveModeEnum.ALL)
@CheckBizCode(bizTypeCode = "drctDbtTxInf.pmtTpInf.lclInstrm.prtry", bizCtgyCode = "drctDbtTxInf.purp.prtry")
@CheckAccountTag(type = Type.WID, path = {"drctDbtTxInf.cdtrAcct.id.othr.id"})
@CheckAccountTag(type = Type.SUB_WALLET_TOKEN, path = {"drctDbtTxInf.rmtInf.authInfo"})
public class Dcep21101001DTO extends GwDTO implements ClearingDTO, DataEncryption {

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
	@JacksonXmlProperty(localName = "DrctDbtTxInf")
	@NotNull
	@Valid
	private DrctDbtTxInf drctDbtTxInf;
	/**
	 * @see com.dcep.common.model.GwDTO#init()
	 */
	@Override
	public void init() {
		rmtInf(this.drctDbtTxInf.getUstrds());
	}

	/**
	 * 将ustrds附言字段的值赋值到RmtInf对象
	 * @param ustrds
	 */
	void rmtInf(List<String> ustrds) {
		if (ustrds != null && !ustrds.isEmpty()) {
			this.getDrctDbtTxInf().setRmtInf(new RmtInf(ustrds));
		}
	}

	/**
	 * @see com.dcep.common.model.GwDTO#fetchMsgId()
	 */
	@Override
	public String fetchMsgId() {
		return grpHdr.getMsgId();
	}


	@Override
	public boolean check(SoapHeader soapHeader) {
		// 报文头报文标识号
		String headerMsgId = soapHeader.getMsgSN().substring(0, 32);
		// 报文体报文标识号
		String bodyMsgId = grpHdr.getMsgId();
		if (!headerMsgId.equals(bodyMsgId)) {
			throw new DcepException(ErrorEnum.MSGSN_MSGID_NOT_MATCH_ERROR);
		}

		// 校验msgId的(13,16)这3位与msgTp的中间3位是否一致 报文编号校验11
		String bodyMsgIdMsgTp = bodyMsgId.substring(12, 15);
		String msgTpNum = soapHeader.getMsgTp().substring(5, 8);
		if (!msgTpNum.equals(bodyMsgIdMsgTp)) {
			throw new DcepException(ErrorEnum.MSGTP_NOT_IN_MSGID_ERROR);
		}
		
		// 收款人钱包等级必输
		if (null == drctDbtTxInf.getCdtrAcct().getId().getOthr().getSchmeNm()) {
			throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "收款人钱包等级必输");
		}

		// 校验附言Ustrds里标签字段
		ValidateUtils.validate(this.getDrctDbtTxInf().getRmtInf());

		//校验钱包是压测钱包必须收付款方都是压测，非压测钱包收付款方都非压测
		if (!DtoCheckUtil.checkDbCrWltId(this.clrDbtrWltId(), this.clrCdtrWltId())) {
			throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(),
					"付款人钱包和收款钱包需同时都为压测钱包或都为非压测钱包");
		}

		return DtoCheckUtil.checkPayeeMsgInst(soapHeader.getSender(), this.clrDbtrPtyId(), soapHeader.getReceiver(),
				this.clrCdtrPtyId());

	}

	@Override
	public com.dcep.common.model.soap.OrgnlGrpHdr presumeConfirm() {
		com.dcep.common.model.soap.OrgnlGrpHdr orgnlGrpHdr = new com.dcep.common.model.soap.OrgnlGrpHdr(); 
		orgnlGrpHdr.setOrgnlInstgPty(this.drctDbtTxInf.getCdtrAgt().getFinInstnId().getClrSysMmbId().getMmbId());
		orgnlGrpHdr.setOrgnlMsgId(this.getGrpHdr().getMsgId());
		orgnlGrpHdr.setOrgnlMT(MsgTpEnum.DBT_REQUEST.getCode());
		return orgnlGrpHdr;
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
		return MsgTpEnum.DBT_REQUEST.getCode();
	}

	@Override
	public String recMsgId() {
		return grpHdr.getMsgId();
	}

	@Override
	public String clrDbtrWltId() {
		if (null != drctDbtTxInf.getRmtInf()) {
			return drctDbtTxInf.getRmtInf().getAuthInfo();
		}
		return null;
	}

	@Override
	public String clrCdtrWltId() {
		return drctDbtTxInf.getCdtrAcct().getId().getOthr().getId();
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
		return MsgTpEnum.DBT_REQUEST.getCode();
	}

	@Override
	public String clrEndToEndId() {
		return drctDbtTxInf.getPmtId().getEndToEndId();
	}

	@Override
	public String getClrMsgId() {
		return grpHdr.getMsgId();
	}

	@Override
	public String clrBizTp() {
		return drctDbtTxInf.getPmtTpInf().getLclInstrm().getPrtry();
	}

	@Override
	public String clrBizKind() {
		return drctDbtTxInf.getPurp().getPrtry();
	}

	@Override
	public String clrDbtrPtyId() {
		return drctDbtTxInf.getDbtrAgt().getFinInstnId().getClrSysMmbId().getMmbId();
	}

	@Override
	public String clrDbtrSysId() {
		return ChnlSysEnum.DCEP.getCode();
	}

	@Override
	public String clrCdtrPtyId() {
		return drctDbtTxInf.getCdtrAgt().getFinInstnId().getClrSysMmbId().getMmbId();
	}

	@Override
	public String clrCdtrSysId() {
		return ChnlSysEnum.DCEP.getCode();
	}

	@Override
	public String clrCurrency() {
		return drctDbtTxInf.getIntrBkSttlmAmt().getCcy();
	}

	@Override
	public String clrAmt() {
		return drctDbtTxInf.getIntrBkSttlmAmt().getValue();
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
		// 交易描述信息(付款人签约协议号)
		rmtInf(this.getDrctDbtTxInf().getUstrds());
		RmtInf rmtInf = this.drctDbtTxInf.getRmtInf();
		if (rmtInf != null) {
           return rmtInf.getAuthInfo();
		}
		return null;
	}

	@Override
	public String clrFlag() {
		return ClrFlgEnum.YES.getCode();
	}

	@Override
	public void clrBatId(String clrBatId) {
		drctDbtTxInf.getPmtId().setInstrId(clrBatId);
	}

	@Override
	public String clrBatId() {
		return drctDbtTxInf.getPmtId().getInstrId();
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
		return drctDbtTxInf.getCdtrAgt().getFinInstnId().getClrSysMmbId().getMmbId();
	}

	@Override
	public String clrSendSysId() {
		return ChnlSysEnum.DCEP.getCode();
	}

	@Override
	public String clrRecvPtyId() {
		return drctDbtTxInf.getDbtrAgt().getFinInstnId().getClrSysMmbId().getMmbId();
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
		drctDbtTxInf.getPmtId().setInstrId(response.getResult().getBatchId());
	}


	@Override
	public String clrAcctTp() {
		return ClrAcctTpEnum.PAY.getCode();
	}

    public List<String> fetchEncryptionFeatures() {
        // 获取需加解密处理的敏感要素，若为空则赋值为null，保证前后顺序一致
        List<String> data = new ArrayList<>();

	    if (drctDbtTxInf != null && drctDbtTxInf.getCdtr() != null && drctDbtTxInf.getCdtr().getNm() != null) {
		    data.add(drctDbtTxInf.getCdtr().getNm());
	    }

	    if (drctDbtTxInf != null && drctDbtTxInf.getCdtrAcct() != null
			    && drctDbtTxInf.getCdtrAcct().getId() != null
			    && drctDbtTxInf.getCdtrAcct().getId().getOthr() != null
			    && drctDbtTxInf.getCdtrAcct().getId().getOthr().getId() != null) {
		    data.add(drctDbtTxInf.getCdtrAcct().getId().getOthr().getId());
	    }

	    if (drctDbtTxInf != null && drctDbtTxInf.getDbtr() != null && drctDbtTxInf.getDbtr().getNm() != null) {
            data.add(drctDbtTxInf.getDbtr().getNm());
        }

	    if (drctDbtTxInf != null && drctDbtTxInf.getDbtrAcct() != null
			    && drctDbtTxInf.getDbtrAcct().getId() != null
			    && drctDbtTxInf.getDbtrAcct().getId().getOthr() != null
			    && drctDbtTxInf.getDbtrAcct().getId().getOthr().getId() != null) {
		    data.add(drctDbtTxInf.getDbtrAcct().getId().getOthr().getId());
	    }

        return data;
    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
		int index = 0;
        // 按顺序赋值加解密处理后的敏感要素
        if (drctDbtTxInf != null && drctDbtTxInf.getCdtr() != null && drctDbtTxInf.getCdtr().getNm() != null) {
            drctDbtTxInf.getCdtr().setNm(encryptionFeatures.get(index++));
        }

        if (drctDbtTxInf != null && drctDbtTxInf.getCdtrAcct() != null
				&& drctDbtTxInf.getCdtrAcct().getId() != null
                && drctDbtTxInf.getCdtrAcct().getId().getOthr() != null
		        && drctDbtTxInf.getCdtrAcct().getId().getOthr().getId() != null) {
            drctDbtTxInf.getCdtrAcct().getId().getOthr().setId(encryptionFeatures.get(index++));
        }

        if (drctDbtTxInf != null && drctDbtTxInf.getDbtr() != null && drctDbtTxInf.getDbtr().getNm() != null) {
            drctDbtTxInf.getDbtr().setNm(encryptionFeatures.get(index++));
        }

        if (drctDbtTxInf != null && drctDbtTxInf.getDbtrAcct() != null
				&& drctDbtTxInf.getDbtrAcct().getId() != null
                && drctDbtTxInf.getDbtrAcct().getId().getOthr() != null
		        && drctDbtTxInf.getDbtrAcct().getId().getOthr().getId() != null) {
            drctDbtTxInf.getDbtrAcct().getId().getOthr().setId(encryptionFeatures.get(index++));
        }
    }

	@Override
	public String clrSysWorkDt(){
		if (drctDbtTxInf != null && drctDbtTxInf.getUstrds() != null) {
			for (String ustrd : drctDbtTxInf.getUstrds()){
				if (ustrd.contains("/SysWorkDt/")){
					return ustrd.substring("/SysWorkDt/".length());
				}
			}
		}
		return null;
	}
}
