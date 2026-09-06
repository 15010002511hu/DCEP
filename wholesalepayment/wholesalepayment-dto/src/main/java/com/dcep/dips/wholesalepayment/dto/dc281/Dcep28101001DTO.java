/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc281;

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
import com.dcep.dips.wholesalepayment.dto.ContractInfo;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.DtoCheckUtil;
import com.dcep.infocache.validation.CheckClrGrpHdr;
import com.dcep.infocache.validation.CheckClrOrgnlGrpHdr;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
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

@JacksonXmlRootElement(localName = "ConRefReq", namespace = "http://www.dcep.com/dcep/28101001/")
@Setter
@Getter
@ToString(callSuper = true)
@Gateway(msgTp = "dcep.281.010.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {
		@RpcInfo(name = Constant.NAME_PAYMENT, methods = { @GwMethod(name = Constant.DBTR_SETTLE) }) }))
@Clearing(presumeStatus = ClearingStatusEnum.PRESUME_SUCCESS, action = ClearingActionEnum.PREPARE, instgDrctPty = InstgDrctPtyEnum.DBTR, checkMode = ClearingCheckModeEnum.PAYER_CHECK, cdtDbtInd = ClearingProdCdtDbtIndEnum.CRDT)
@Record(saveMode = RecordSaveModeEnum.ALL)
@CheckBizCode(bizTypeCode = "trxInf.trxBizTp", bizCtgyCode = "trxInf.trxCtgyPurpCd")
@CheckAccountTag(type = Type.WID, path = {"dbtrInf.dbtrWltId", "cdtrInf.cdtrWltId"})
public class Dcep28101001DTO extends GwDTO implements ClearingDTO, DataEncryption {

	/**  */
	private static final long serialVersionUID = 4384316990011927276L;

	/**
	 * 【业务头组件】
	 */
	@JacksonXmlProperty(localName = "GrpHdr")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	@CheckGrpHdrMsgId(groups = Priority.Lowest.class)
	@CheckClrGrpHdr(groups = Priority.Lowest.class)
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
	 * 【交易信息】
	 */
	@JacksonXmlProperty(localName = "TrxInf")
	@NotNull
	@Valid
	private TrxInf trxInf;

	/**
	 * 【付款人信息】
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
	 * 【商户信息】
	 */
	@JacksonXmlProperty(localName = "MrchntInf")
	@Valid
	private MrchntInf mrchntInf;

	/**
	 * 【原订单信息】
	 */
	@JacksonXmlProperty(localName = "OrgnlOrdrInf")
	@NotNull
	@Valid
	private OrgnlOrdrInf orgnlOrdrInf;
	
	/**
	 * 【受理服务机构信息】
	 */
	@JacksonXmlProperty(localName = "AcqAgtInf")
	@Valid
	private AcqAgtInf acqAgtInf;
	
	/**
     * 【二级商户退款列表】
     */
    @JacksonXmlElementWrapper(localName = "SubMrchntRefList")
    @JacksonXmlProperty(localName = "SubMrchntRefInf")
    @Valid
	private List<SubMrchntRefInf> subMrchntRefInf;

	/**
	 * 【退款权益信息】
	 */
	@JacksonXmlProperty(localName = "CpnInf")
	@Valid
	private CpnInf cpnInf;

	/**
	 * 付款人为合约实例信息
	 */
	@JacksonXmlProperty(localName = "DbtrCtrctInst")
	@Valid
	private DbtrCtrctInst dbtrCtrctInst;

	/**
	 * 收款人为合约实例信息
	 */
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "CdtrCtrctInst")
	@Valid
	private List<CdtrCtrctInst> cdtrCtrctInstList;

	@Override
	public String clrMsgTp() {
		// 报文编号
		return MsgTpEnum.REFUND_REQUREST.getCode();
	}

	@Override
	public String clrEndToEndId() {
		return null;
	}

	@Override
	public String getClrMsgId() {
		// 报文标识号
		return grpHdr.getMsgId();
	}

	@Override
	public String clrBizTp() {
		// 业务类型编码
		return trxInf.getTrxBizTp();
	}

	@Override
	public String clrBizKind() {
		// 业务种类编码
		return trxInf.getTrxCtgyPurpCd();
	}

	@Override
	public String clrDbtrPtyId() {
		// 付款运营机构
		return dbtrInf.getDbtrPtyId();
	}

	@Override
	public String clrCdtrPtyId() {
		// 收款运营机构
		return grpHdr.getInstdPty().getInstdDrctPty();
	}

	@Override
	public String clrCurrency() {
		// 交易币种
		return trxInf.getTrxAmt().getCcy();
	}

	@Override
	public String clrAmt() {
		// 交易金额
		return trxInf.getTrxAmt().getValue();
	}

	@Override
	public String clrBizRspSts() {
		// 业务回执状态clrBizRspSts
		return null;
	}

	@Override
	public String clrBizRjctCd() {
		// 业务拒绝码
		return null;
	}

	@Override
	public String clrRjctResn() {
		// 业务拒绝原因
		return null;
	}

	@Override
	public String clrTrxInf() {
		// 交易描述信息(退款报文为原报文标识号)
		return orgnlGrpHdr.getOrgnlMsgId();
	}

	@Override
	public String clrFlag() {
		return ClrFlgEnum.YES.getCode();
	}

	@Override
	public void fillBatchId(Response<ClearingStatus> response) {
		// 向报文中赋交易批次号
		trxInf.setBatchId(response.getResult().getBatchId());
	}

	@Override
	public OrgnlGrpHdr presumeConfirm() {
		OrgnlGrpHdr orgnlGrpHdr = new OrgnlGrpHdr();
		orgnlGrpHdr.setOrgnlInstgPty(this.getGrpHdr().getInstgPty().getInstgDrctPty());
		orgnlGrpHdr.setOrgnlMsgId(this.getGrpHdr().getMsgId());
		orgnlGrpHdr.setOrgnlMT(MsgTpEnum.REFUND_REQUREST.getCode());
		return orgnlGrpHdr;
	}

	/**
	 * --------------RecordDTO接口方法-------------
	 */
	@Override
	public String encode() {
		// 对象转换成档案记录JSON串
		return JSON.toJSONString(this);
	}

	@Override
	public RecordDTO decode(String encode) {
		return JSONObject.toJavaObject(JSON.parseObject(encode), this.getClass());
	}

	@Override
	public String recMsgTp() {
		// 报文编号
		return MsgTpEnum.REFUND_REQUREST.getCode();
	}

	@Override
	public String recMsgId() {
		// 报文标识号
		return grpHdr.getMsgId();
	}

	@Override
	public String clrDbtrWltId() {
		// 付款方钱包id
		return dbtrInf.getDbtrWltId();
	}

	@Override
	public String clrDbtrSysId() {
		return ChnlSysEnum.DCEP.getCode();
	}

	@Override
	public String clrCdtrWltId() {
		// 收款方钱包id
		if (null != cdtrInf) {
			return cdtrInf.getCdtrWltId();
		}
		return null;
	}

	@Override
	public String clrCdtrSysId() {
		return ChnlSysEnum.DCEP.getCode();
	}

	@Override
	public String recOrgnlMsgTp() {
		// 原报文编号
		return null;
	}

	@Override
	public String recOrgnlMsgId() {
		// 原报文标识号
//		return null;
		 return orgnlGrpHdr.getOrgnlMsgId();
	}

	/**
	 * --------------GwDTO接口方法-------------
	 */

	/**
	 * @see GwDTO#init()
	 */
	@Override
	public void init() {

	}

	/**
	 * @see GwDTO#fetchMsgId() 获取msgId
	 */
	@Override
	public String fetchMsgId() {
		return grpHdr.getMsgId();
	}

	@Override
	public boolean check(SoapHeader soapHeader) {
        if (!MsgTpEnum.DBT_REQUEST.getCode().equals(orgnlGrpHdr.getOrgnlMT())) {
            if (StringUtils.isBlank(cdtrInf.getCdtrWltId()) || StringUtils.isBlank(cdtrInf.getCdtrWltNm())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "原退款报文类型为非收款报文时，收款人钱包ID及名称必填");
            }
        }

		return !CheckUtils.requestMsgChk(soapHeader, grpHdr) ? false
				: DtoCheckUtil.checkPayerMsgInst(soapHeader.getSender(), dbtrInf.getDbtrPtyId(),
						soapHeader.getReceiver(), cdtrInf.getCdtrPtyId());
	}

	@Override
	public void clrBatId(String clrBatId) {
		// 交易批次号
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
	public String clrAcctTp() {
		return ClrAcctTpEnum.PAY.getCode();
	}

	@Override
	public ContractInfo clrContractInfo(){
		ContractInfo contractInfo = new ContractInfo();
		contractInfo.setHasContract(false);
		contractInfo.setTransType(TransTypeEnum.REFUND);
		if (dbtrCtrctInst != null) {
			contractInfo.setCdtrContractId(dbtrCtrctInst.getCtrctId());
			contractInfo.setCdtrWalletId(clrDbtrWltId());
			contractInfo.setHasContract(true);
		}
		if (cdtrCtrctInstList != null
			&& cdtrCtrctInstList.size() > 0) {
			List<String> senderContractIds = new ArrayList<>();
			for (CdtrCtrctInst cdtrCtrctInst : cdtrCtrctInstList) {
				senderContractIds.add(cdtrCtrctInst.getCtrctId());
			}
			contractInfo.setDbtrContractIds(senderContractIds);
			contractInfo.setDbtrWalletId(clrCdtrWltId());
			contractInfo.setHasContract(true);
		}
		return contractInfo;
	}

    public List<String> fetchEncryptionFeatures() {
        // 获取需加解密处理的敏感要素，若为空则赋值为null，保证前后顺序一致
	    List<String> data = new ArrayList<>();
	    if (null != dbtrInf && null != dbtrInf.getDbtrNm()) {
		    data.add(dbtrInf.getDbtrNm());
	    }
	    if (null != dbtrInf && null != dbtrInf.getDbtrWltId()) {
		    data.add(dbtrInf.getDbtrWltId());
	    }
	    if (null != cdtrInf && null != cdtrInf.getCdtrNm()) {
		    data.add(cdtrInf.getCdtrNm());
	    }
	    if (null != cdtrInf && null != cdtrInf.getCdtrWltId()) {
		    data.add(cdtrInf.getCdtrWltId());
	    }
	    return data;
    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
	    int index = 0;
	    // 按顺序赋值加解密处理后的敏感要素
	    if (null != dbtrInf && null != dbtrInf.getDbtrNm()) {
		    dbtrInf.setDbtrNm(encryptionFeatures.get(index++));
	    }
	    if (null != dbtrInf && null != dbtrInf.getDbtrWltId()) {
		    dbtrInf.setDbtrWltId(encryptionFeatures.get(index++));
	    }
	    if (null != cdtrInf && null != cdtrInf.getCdtrNm()) {
		    cdtrInf.setCdtrNm(encryptionFeatures.get(index++));
	    }
	    if (null != cdtrInf && null != cdtrInf.getCdtrWltId()) {
		    cdtrInf.setCdtrWltId(encryptionFeatures.get(index++));
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
