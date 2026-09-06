/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc201;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.CheckAccountTag;
import com.dcep.common.annotation.CheckAccountTag.Type;
import com.dcep.common.annotation.CheckBizCode;
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
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.constants.Constant;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.ContractInfo;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.dto.common.Common;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.DtoCheckUtil;
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


@JacksonXmlRootElement(localName = "FIToFICstmrCdtTrf", namespace = "http://www.dcep.com/dcep/20101001/")
@Setter
@Getter
@ToString(callSuper = true)
@Gateway(msgTp = "dcep.201.010.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {
		@RpcInfo(name = Constant.NAME_PAYMENT, methods = { @GwMethod(name = Constant.DBTR_SETTLE) }) }))
@Clearing(presumeStatus = ClearingStatusEnum.PRESUME_SUCCESS, action = ClearingActionEnum.PREPARE, instgDrctPty = InstgDrctPtyEnum.DBTR, checkMode = ClearingCheckModeEnum.PAYER_CHECK, cdtDbtInd = ClearingProdCdtDbtIndEnum.CRDT)
@Record(saveMode = RecordSaveModeEnum.ALL)
//@CheckClrBatId
@CheckBizCode(bizTypeCode = "cdtTrfTxInf.pmtTpInf.ctgyPurp.prtry", bizCtgyCode = "cdtTrfTxInf.purp.prtry")
@CheckAccountTag(type = Type.WID, path = {"cdtTrfTxInf.dbtrAcct.id.othr.id", "cdtTrfTxInf.cdtrAcct.id.othr.id"})
public class Dcep20101001DTO extends GwDTO implements ClearingDTO, DataEncryption {

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
		return MsgTpEnum.CDT_REQUEST.getCode();
	}

	@Override
	public String recMsgId() {
		return grpHdr.getMsgId();
	}

	@Override
	public String clrDbtrWltId() {
		return cdtTrfTxInf.getDbtrAcct().getId().getOthr().getId();
	}

	@Override
	public String clrCdtrWltId() {
		return cdtTrfTxInf.getCdtrAcct() != null ? cdtTrfTxInf.getCdtrAcct().getId().getOthr().getId() : null;
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

		return MsgTpEnum.CDT_REQUEST.getCode();
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
	public String clrDbtrSysId() {
		return ChnlSysEnum.DCEP.getCode();
	}

	@Override
	public String clrCdtrPtyId() {
		return cdtTrfTxInf.getInstdAgt().getFinInstnId().getClrSysMmbId().getMmbId();
	}

	@Override
	public String clrCdtrSysId() {
		return ChnlSysEnum.DCEP.getCode();
	}

	@Override
	public String clrCurrency() {
		return cdtTrfTxInf.getIntrBkSttlmAmt().getCcy();
	}

	@Override
	public String clrAmt() {
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
	 * @see com.dcep.common.model.GwDTO#init()
	 */
	@Override
	public void init() {
		rmtInf(this.getCdtTrfTxInf().getUstrds());
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
		cdtTrfTxInf.getPmtId().setInstrId(response.getResult().getBatchId());
	}

	public Dcep20101001DTO() {

	}

	/**
	 * @param grpHdr
	 * @param cdtTrfTxInf
	 */
	public Dcep20101001DTO(GrpHdr grpHdr, CdtTrfTxInf cdtTrfTxInf) {
		this.grpHdr = grpHdr;
		this.cdtTrfTxInf = cdtTrfTxInf;
	}

	@Override
	public OrgnlGrpHdr presumeConfirm() {
		OrgnlGrpHdr orgnlGrpHdr = new OrgnlGrpHdr();
		orgnlGrpHdr.setOrgnlInstgPty(this.cdtTrfTxInf.getInstgAgt().getFinInstnId().getClrSysMmbId().getMmbId());
		orgnlGrpHdr.setOrgnlMsgId(this.getGrpHdr().getMsgId());
		orgnlGrpHdr.setOrgnlMT(MsgTpEnum.CDT_REQUEST.getCode());
		return orgnlGrpHdr;

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

		// 付款人钱包类型,钱包类型为WT09或WT10的时候掩码邮箱和掩码手机号可以为空,为WL01或WL02的时候在判断钱包等级
		Tp tp = cdtTrfTxInf.getDbtrAcct().getTp();
		// 付款人钱包等级
        SchmeNm schmeNm = cdtTrfTxInf.getDbtrAcct().getId().getOthr().getSchmeNm();
		String payerWltTp = null;
		if (tp != null) {
			payerWltTp = tp.getPrtry();
			if (payerWltTp.matches("WT01||WT02")) {
				// 掩码手机号
				String maskedTel = null;
				// 掩码邮箱
				String maskedEmail = null;
				if(cdtTrfTxInf.getRmtInf() != null) {
					maskedTel = cdtTrfTxInf.getRmtInf().getMaskedTel();
					maskedEmail = cdtTrfTxInf.getRmtInf().getMaskedEmail();
				}

				String payerWltLvl = null;
				if (schmeNm != null) {
					payerWltLvl = schmeNm.getPrtry();
				}

				if (StringUtils.isNotBlank(payerWltLvl) && payerWltLvl.matches("WL01||WL02||WL03")) {
					if (StringUtils.isBlank(maskedTel)) {
						throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "付款人钱包等级为WL01或WL02或WL03时，掩码手机号字段必填");
					}
				} else if ("WL04".equals(payerWltLvl)) {
					if (StringUtils.isBlank(maskedTel) && StringUtils.isBlank(maskedEmail)) {
						throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "付款人钱包等级为WL04时，掩码手机号和掩码邮箱不能同时为空");
					}
				}
			}
		}
		
        // 红包业务类型，红包ID必输
        RmtInf rmtInf = this.getCdtTrfTxInf().getRmtInf();
		String bizTp = this.getCdtTrfTxInf().getPmtTpInf().getCtgyPurp().getPrtry();
        if (Common.PURP_RED_PACKET_NEW.equals(bizTp)|| Common.PURP_RED_PACKET.equals(bizTp)) {
            if (rmtInf == null || (rmtInf != null && StringUtils.isBlank(rmtInf.getRedPktId()))) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "业务类型为红包时，红包ID必填");
            }
        }

		// 校验附言Ustrds里标签字段
		ValidateUtils.validate(this.getCdtTrfTxInf().getRmtInf());

		//校验钱包是压测钱包必须收付款方都是压测，非压测钱包收付款方都非压测
		if (!DtoCheckUtil.checkDbCrWltId(this.clrDbtrWltId(), this.clrCdtrWltId())) {
			throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(),
					"付款人钱包和收款钱包需同时都为压测钱包或都为非压测钱包");
		}
		
        if (NacosConsume.getCredttmInterval().contains(MsgTpEnum.CDT_REQUEST_ABBR.getCode())) {
            // 用户居民类型必填
            if (rmtInf == null || (rmtInf != null && StringUtils.isBlank(rmtInf.getResdtTp()))) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "付款用户居民类型必填");
            }
            // 用户常驻国家/地区代码必填
            if (rmtInf == null
                    || (rmtInf != null && StringUtils.isBlank(rmtInf.getResdtCtryCd()))) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "付款用户常驻国家/地区代码必填");
            }
            // 个人钱包注册手机号所在国家/地区代码必填
            if (tp != null && tp.getPrtry().matches("WT01||WT02") && (rmtInf == null
                    || (rmtInf != null && StringUtils.isBlank(rmtInf.getRegrCtryCd())))) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(),
                        "付款钱包注册手机号所在国家/地区代码必填");
            }

            if (schmeNm != null && "WL04".equals(schmeNm.getPrtry())
                    && StringUtils.isBlank(cdtTrfTxInf.getDbtrAcct().getNm())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "付款钱包等级为四类时，付款钱包名称必填");
            }
            // 收款钱包等级
            SchmeNm cdtrSchmeNm = cdtTrfTxInf.getCdtrAcct().getId().getOthr().getSchmeNm();
            Cdtr cdtr = cdtTrfTxInf.getCdtr();
            if (cdtrSchmeNm != null && "WL04".equals(cdtrSchmeNm.getPrtry())
                    && StringUtils.isBlank(cdtTrfTxInf.getCdtrAcct().getNm())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "收款钱包等级为四类时，收款钱包名称必填");
            }
            if (cdtrSchmeNm != null && "WL03".compareTo(cdtrSchmeNm.getPrtry()) >= 0
                    && (cdtr == null || StringUtils.isBlank(cdtr.getNm()))) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "收款钱包等级为一类、二类、三类时，收款人名称必填");
            }
        }

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
		return cdtTrfTxInf.getInstgAgt().getFinInstnId().getClrSysMmbId().getMmbId();
	}

	@Override
	public String clrSendSysId() {
		return ChnlSysEnum.DCEP.getCode();
	}

	@Override
	public String clrRecvPtyId() {
		return cdtTrfTxInf.getInstdAgt().getFinInstnId().getClrSysMmbId().getMmbId();
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
		contractInfo.setTransType(TransTypeEnum.TRANSFER_MONEY);
		if (cdtTrfTxInf != null
			&& cdtTrfTxInf.getSplmtryData() != null
			&& cdtTrfTxInf.getSplmtryData().getEnvlp() != null
			&& cdtTrfTxInf.getSplmtryData().getEnvlp().getCdtrCtrctInst() != null) {
			contractInfo.setCdtrContractId(cdtTrfTxInf.getSplmtryData().getEnvlp().getCdtrCtrctInst().getCtrctId());
			contractInfo.setCdtrWalletId(clrCdtrWltId());
			contractInfo.setHasContract(true);
		}
		if (cdtTrfTxInf != null
			&& cdtTrfTxInf.getSplmtryData() != null
			&& cdtTrfTxInf.getSplmtryData().getEnvlp() != null
			&& cdtTrfTxInf.getSplmtryData().getEnvlp().getDbtrCtrctInst() != null) {
			List<String> senderContractIds = new ArrayList<>();
			senderContractIds.add(cdtTrfTxInf.getSplmtryData().getEnvlp().getDbtrCtrctInst().getCtrctId());
			contractInfo.setDbtrContractIds(senderContractIds);
			contractInfo.setDbtrWalletId(clrDbtrWltId());
			contractInfo.setHasContract(true);
		}
		return contractInfo;
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
        if (cdtTrfTxInf != null && cdtTrfTxInf.getUstrds() != null) {
            for (String ustrd : cdtTrfTxInf.getUstrds()) {
                if (ustrd.contains("/MaskedTel/")) {
					data.add(ustrd.substring("/MaskedTel/".length()));
                } else if (ustrd.contains("/MaskedEmail/")) {
					data.add(ustrd.substring("/MaskedEmail/".length()));
                }
            }
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

        if (cdtTrfTxInf != null && cdtTrfTxInf.getUstrds() != null) {
            for (String ustrd : cdtTrfTxInf.getUstrds()) {
                if (ustrd.contains("/MaskedTel/")) {
                    cdtTrfTxInf.getUstrds().set(cdtTrfTxInf.getUstrds().indexOf(ustrd),
                            "/MaskedTel/" + encryptionFeatures.get(index++));
                } else if (ustrd.contains("/MaskedEmail/")) {
                    cdtTrfTxInf.getUstrds().set(cdtTrfTxInf.getUstrds().indexOf(ustrd),
                            "/MaskedEmail/" + encryptionFeatures.get(index++));
                }
            }
        }
    }

	@Override
	public String clrBizPrty(){
		return cdtTrfTxInf.getSttlmPrty();
	}

	@Override
	public String clrSysWorkDt(){
		if (cdtTrfTxInf != null && cdtTrfTxInf.getUstrds() != null) {
			for (String ustrd : cdtTrfTxInf.getUstrds()){
				if (ustrd.contains("/SysWorkDt/")){
					return ustrd.substring("/SysWorkDt/".length());
				}
			}
		}
		return null;
	}

	@Override
	public void fillSttlmDt(String sttlmDt) {
		if (cdtTrfTxInf != null && cdtTrfTxInf.getUstrds() != null) {
			cdtTrfTxInf.getUstrds().add("/SttlmDt/" + sttlmDt);
		}
	}
}
