/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc263;

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
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.constants.Constant;
import com.dcep.dips.wholesalepayment.dto.*;
import com.dcep.dips.wholesalepayment.enums.*;
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


@JacksonXmlRootElement(localName = "ConRsNtfctn", namespace = "http://www.dcep.com/dcep/26301001/")
@Setter
@Getter
@ToString(callSuper = true)
@Gateway(msgTp = "dcep.263.010.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {
		@RpcInfo(name = Constant.NAME_PAYMENT, methods = { @GwMethod(name = Constant.RESULT_REPORT) }) }))
@Clearing(intervalSecond = 5, notityTimeout = 600)
@Record(saveMode = RecordSaveModeEnum.ALL)
@CheckAccountTag(type = Type.WID, path = {"dbtr.dbtrWltId"})
public class Dcep26301001DTO extends GwDTO implements ClearingDTO, DataEncryption {

	/**  */
	private static final long serialVersionUID = 1507532276004308813L;

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
	 * 【ResponsionInformation】
	 */
	@JacksonXmlProperty(localName = "RspsnInf")
	@NotNull
	@Valid
	private RspsnInf rspsnInf;
	/**
	 * 【交易信息】
	 */
	@JacksonXmlProperty(localName = "TrxInf")
	@NotNull
	@Valid
	private TrxInf trxInf;
	/**
	 * 【收款钱包信息】
	 */
	@JacksonXmlProperty(localName = "Cdtr")
	@NotNull
	@Valid
	private Cdtr cdtr;
	/**
	 * 【付款钱包信息】
	 */
	@JacksonXmlProperty(localName = "Dbtr")
	@NotNull
	@Valid
	private Dbtr dbtr;

	/**
	 * 【营销信息】
	 */
	@JacksonXmlProperty(localName = "PrmtInf")
	@Valid
	private PrmtInf prmtInf;

	/**
	 * 收款人为合约实例信息
	 */
	@JacksonXmlProperty(localName = "CdtrCtrctInst")
	@Valid
	private CdtrCtrctInst cdtrCtrctInst;

	/**
	 * 付款人为合约实例信息
	 */
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "DbtrCtrctInst")
	@Valid
	private List<DbtrCtrctInst> dbtrCtrctInstList;
	
	public Dcep26301001DTO() {
	}

	public Dcep26301001DTO(GrpHdr grpHdr, OrgnlGrpHdr orgnlGrpHdr, RspsnInf rspsnInf, TrxInf trxInf, Cdtr cdtr,
						   Dbtr dbtr) {
		this.grpHdr = grpHdr;
		this.orgnlGrpHdr = orgnlGrpHdr;
		this.rspsnInf = rspsnInf;
		this.trxInf = trxInf;
		this.cdtr = cdtr;
		this.dbtr = dbtr;
	}

	@Override
	public String clrMsgTp() {
		// 报文编号
		return MsgTpEnum.ORDR_CONF_RESULT_NOTICE.getCode();
	}

	@Override
	public String clrEndToEndId() {
		return null;
	}

	@Override
	public String getClrMsgId() {
		// 报文标识号
		return orgnlGrpHdr.getOrgnlMsgId();
	}

	@Override
	public String clrBizTp() {
		// 业务类型编码
		return null;
	}

	@Override
	public String clrBizKind() {
		// 业务种类编码
		return null;
	}

	@Override
	public String clrDbtrPtyId() {
		// 付款运营机构(付款方为发送方)
		return grpHdr.getInstgPty().getInstgDrctPty();
	}

	@Override
	public String clrCdtrPtyId() {
		// 收款运营机构(收款方为接收方)
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
		return rspsnInf.getRspsnSts();
	}

	@Override
	public String clrBizRjctCd() {
		// 业务拒绝码
		return rspsnInf.getRjctCd();
	}

	@Override
	public String clrRjctResn() {
		// 业务拒绝原因
		return rspsnInf.getRjctInf();
	}

	@Override
	public String clrTrxInf() {
		// 交易描述信息(消费结果通知报文为付款人钱包ID-付款人钱包名称)
		return dbtr.getDbtrWltId() + "-" + dbtr.getDbtrWltNm();
	}

	@Override
	public String clrFlag() {
		return null;
	}

	@Override
	public void fillBatchId(Response<ClearingStatus> response) {
		// 向报文中赋交易批次号
		trxInf.setBatchId(response.getResult().getBatchId());
	}

	@Override
	public void fillPlatPrcSts(String bizSts) {
		// 赋值业务状态(平台)
		rspsnInf.setPrcSts(bizSts);
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
		return MsgTpEnum.ORDR_CONF_RESULT_NOTICE.getCode();
	}

	@Override
	public String recMsgId() {
		// 通知类报文档案表标识号为原报文标识号
		return orgnlGrpHdr.getOrgnlMsgId();
	}

	@Override
	public String clrDbtrWltId() {
		// 付款方钱包id
		return dbtr.getDbtrWltId();
	}

	@Override
	public String clrDbtrSysId() {
		return null;
	}

	@Override
	public String clrCdtrWltId() {
		// 收款方钱包id
		return null;
	}

	@Override
	public String clrCdtrSysId() {
		return null;
	}

	@Override
	public String recOrgnlMsgTp() {
		// 原报文编号
		return orgnlGrpHdr.getOrgnlMT();
	}

	@Override
	public String recOrgnlMsgId() {
		// 原报文标识号
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
	    // 赋值业务状态(平台)
        rspsnInf.setPrcSts(rspsnInf.getRspsnSts());

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
		//返回营销信息里必填字段校验
		if (null != prmtInf){
			for(CpnInf cpnInf : prmtInf.getCpnInf()){
				// 营销活动类型
				if (StringUtils.isBlank(cpnInf.getPrmtTp())) {
					throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "支付场景下营销活动类型必填");
				}
				// 权益有效期
				if (StringUtils.isBlank(cpnInf.getCpnExp())) {
					throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "支付场景下权益有效期必填");
				}
				// 权益退款属性
				if (StringUtils.isBlank(cpnInf.getCpnRefPrprty())) {
					throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "支付场景下权益退款属性必填");
				}
			}
		}
		
		if (ClearingStatusEnum.SUCCESS.getCode().equals(this.clrBizRspSts())) {
            // 用户居民类型必填
            if ((null != dbtr) && StringUtils.isBlank(dbtr.getResdtTp())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "用户居民类型必填");
            }
            // 用户常驻国家/地区代码必填
            if ((null != dbtr) && StringUtils.isBlank(dbtr.getResdtCtryCd())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "用户常驻国家/地区代码必填");
            }
            // 钱包注册手机号所在国家/地区代码必填
            if ((null != dbtr) && StringUtils.isBlank(dbtr.getRegrCtryCd())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "钱包注册手机号所在国家/地区代码必填");
            }
        }
		
		return CheckUtils.responseMsgChk(soapHeader, grpHdr, orgnlGrpHdr);
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
		return null;
	}

	@Override
	public void clrBizRspSts(String clrBizRspSts) {
		// 业务回执状态
		rspsnInf.setRspsnSts(clrBizRspSts);
	}

	@Override
	public void clrBizRjctCd(String clrBizRjctCd) {
		// 业务拒绝码
		rspsnInf.setRjctCd(clrBizRjctCd);
	}

	@Override
	public void clrRjctResn(String clrRjctResn) {
		// 业务拒绝原因
		rspsnInf.setRjctInf(clrRjctResn);
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
	public OrgnlGrpHdr presumeConfirm() {
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
	public PrmtInf clrPrmtInf() {
		return prmtInf;
	}
	
	@Override
    public String clrResdtTp() {
        return null != dbtr ? dbtr.getResdtTp() : null;
    }

    @Override
    public String clrResdtCtryCd() {
        return null != dbtr ? dbtr.getResdtCtryCd() : null;
    }

    @Override
    public String clrRegrCtryCd() {
        return null != dbtr ? dbtr.getRegrCtryCd() : null;
    }

	@Override
	public ContractInfo clrContractInfo(){
		ContractInfo contractInfo = new ContractInfo();
		contractInfo.setHasContract(false);
		contractInfo.setTransType(TransTypeEnum.MAIN_SCAN);
		if (cdtrCtrctInst != null) {
			contractInfo.setCdtrContractId(cdtrCtrctInst.getCtrctId());
			contractInfo.setCdtrWalletId(clrCdtrWltId());
			contractInfo.setHasContract(true);
		}
		if (dbtrCtrctInstList != null
			&& dbtrCtrctInstList.size() > 0) {
			List<String> senderContractIds = new ArrayList<>();
			for (DbtrCtrctInst dbtrCtrctInst : dbtrCtrctInstList) {
				senderContractIds.add(dbtrCtrctInst.getCtrctId());
			}
			contractInfo.setDbtrContractIds(senderContractIds);
			contractInfo.setDbtrWalletId(clrDbtrWltId());
			contractInfo.setHasContract(true);
		}
		return contractInfo;
	}

    public List<String> fetchEncryptionFeatures() {
        // 获取需加解密处理的敏感要素，若为空则赋值为null，保证前后顺序一致
        List<String> data = new ArrayList<>();
	    if (null != dbtr && null != dbtr.getDbtrWltNm()){
		    data.add(dbtr.getDbtrWltNm());
	    }
	    if (null != dbtr && null != dbtr.getDbtrWltId()){
		    data.add(dbtr.getDbtrWltId());
	    }
        return data;
    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
		int index = 0;
        // 按顺序赋值加解密处理后的敏感要素
	    if (null != dbtr && null != dbtr.getDbtrWltNm()){
		    dbtr.setDbtrWltNm(encryptionFeatures.get(index++));
	    }
	    if (null != dbtr && null != dbtr.getDbtrWltId()){
		    dbtr.setDbtrWltId(encryptionFeatures.get(index++));
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
