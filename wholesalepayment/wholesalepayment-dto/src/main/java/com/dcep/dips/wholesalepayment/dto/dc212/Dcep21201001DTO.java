/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc212;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.ValidateUtils;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.dto.*;
import com.dcep.dips.wholesalepayment.enums.*;
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

@JacksonXmlRootElement(localName = "FIToFIPmtStsRpt",namespace = "http://www.dcep.com/dcep/21201001/")
@Setter
@Getter
@ToString(callSuper = true)
@Gateway(msgTp = "dcep.212.010.01", isReturn = true)
@Record(saveMode = RecordSaveModeEnum.ALL)
public class Dcep21201001DTO extends GwDTO implements ClearingDTO, DataEncryption {

    /**  */
    private static final long serialVersionUID = -7302235204608921494L;

    /**
     * 【业务头组件】
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull
    @Valid
    private GrpHdr grpHdr;

    /**
     * 【原业务头组件】
     */
    @JacksonXmlProperty(localName = "OrgnlGrpInfAndSts")
    @NotNull
    @Valid
    private OrgnlGrpInfAndSts orgnlGrpInfAndSts;

    /**
     * 【业务信息】
     * TransactionInformationAndStatus
     */
    @JacksonXmlProperty(localName = "TxInfAndSts")
    @NotNull
    @Valid
    private TxInfAndSts txInfAndSts;

    /**
     * 【营销信息】
     */
    @JacksonXmlProperty(localName = "PrmtInf")
    @Valid
    private PrmtInf prmtInf;

    /**
     * @see com.dcep.common.model.GwDTO#init()
     */
    @Override
    public void init() {
        rmtInf(this.txInfAndSts.getOrgnlTxRef().getUstrds());
    }

    /**
     * 将ustrds附言字段的值赋值到RmtInf对象
     * @param ustrds
     */
    void rmtInf(List<String> ustrds) {
        if (ustrds != null && !ustrds.isEmpty()) {
            this.getTxInfAndSts().getOrgnlTxRef().setRmtInf(new RmtInf(ustrds));
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
    public void fillPlatPrcSts(String bizSts) {
        //向报文中赋业务状态(平台填写)
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
    	 //原请求报文标识号
        String headerMsgId = soapHeader.getMsgSN().substring(0, 32);
        //原请求报文标识号
        String bodyMsgId = orgnlGrpInfAndSts.getOrgnlMsgId();
        if(!headerMsgId.equals(bodyMsgId)){
            throw new DcepException(ErrorEnum.MSGSN_MSGID_NOT_MATCH_ERROR);
        }

        // 校验附言Ustrds里标签字段
        ValidateUtils.validate(this.getTxInfAndSts().getOrgnlTxRef().getRmtInf());
        
        if (ClearingStatusEnum.SUCCESS.getCode().equals(this.clrBizRspSts())) {
            RmtInf rmtInf = this.getTxInfAndSts().getOrgnlTxRef().getRmtInf();
            // 用户居民类型必填
            if ((null != rmtInf) && StringUtils.isBlank(rmtInf.getResdtTp())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "用户居民类型必填");
            }
            // 用户常驻国家/地区代码必填
            if ((null != rmtInf) && StringUtils.isBlank(rmtInf.getResdtCtryCd())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "用户常驻国家/地区代码必填");
            }
            // 钱包注册手机号所在国家/地区代码必填
            if ((null != rmtInf) && StringUtils.isBlank(rmtInf.getRegrCtryCd())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "钱包注册手机号所在国家/地区代码必填");
            }
        }

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
        return MsgTpEnum.DBT_RESPONSE.getCode();
    }

    @Override
    public String recMsgId() {
        return orgnlGrpInfAndSts.getOrgnlMsgId();
    }

    @Override
    public String clrDbtrWltId() {
        if (txInfAndSts != null && txInfAndSts.getOrgnlTxRef() != null
            && txInfAndSts.getOrgnlTxRef().getDbtrAcct() != null
            && txInfAndSts.getOrgnlTxRef().getDbtrAcct().getId() != null
            && txInfAndSts.getOrgnlTxRef().getDbtrAcct().getId().getOthr() != null
            && txInfAndSts.getOrgnlTxRef().getDbtrAcct().getId().getOthr().getId() != null) {
            return txInfAndSts.getOrgnlTxRef().getDbtrAcct().getId().getOthr().getId();
        } else {
            return null;
        }
    }

    @Override
    public String clrCdtrWltId() {
        return null;
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
        return MsgTpEnum.DBT_RESPONSE.getCode();
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
    public String clrDbtrSysId() {
        return null;
    }

    @Override
    public String clrCdtrPtyId() {
        return txInfAndSts.getOrgnlTxRef().getCdtrAgt().getFinInstnId().getClrSysMmbId().getMmbId();
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
        if (txInfAndSts.getStsRsnInf() != null&&txInfAndSts.getStsRsnInf().getRsn() != null) {
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
        return  txInfAndSts.getOrnlInstrId();
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
		if (txInfAndSts.getStsId() != null) {
			return txInfAndSts.getStsId() + "-"
					+ (ClearingStatusEnum.SUCCESS.getCode().equals(txInfAndSts.getStsId())
							? ClearingPrcCdEnum.BUSI_SUCCESS.getCode()
							: ClearingPrcCdEnum.BUSI_REJT.getCode());
		}
		return "";
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
    public void fillBatchId(Response<ClearingStatus> response) {
        // 向报文中赋交易批次号
        txInfAndSts.setOrnlInstrId(response.getResult().getBatchId());
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
        if (null != this.getTxInfAndSts().getOrgnlTxRef().getRmtInf()) {
            return this.getTxInfAndSts().getOrgnlTxRef().getRmtInf().getResdtTp();
        }
        return null;
    }

    @Override
    public String clrResdtCtryCd() {
        if (null != this.getTxInfAndSts().getOrgnlTxRef().getRmtInf()) {
            return this.getTxInfAndSts().getOrgnlTxRef().getRmtInf().getResdtCtryCd();
        }
        return null;
    }

    @Override
    public String clrRegrCtryCd() {
        if (null != this.getTxInfAndSts().getOrgnlTxRef().getRmtInf()) {
            return this.getTxInfAndSts().getOrgnlTxRef().getRmtInf().getRegrCtryCd();
        }
        return null;
    }

    @Override
    public ContractInfo clrContractInfo() {
        ContractInfo contractInfo = new ContractInfo();
        contractInfo.setHasContract(false);
        contractInfo.setTransType(TransTypeEnum.QUICK_PAY);
        if (txInfAndSts != null
            && txInfAndSts.getSplmtryData() != null
            && txInfAndSts.getSplmtryData().getEnvlp() != null
            && txInfAndSts.getSplmtryData().getEnvlp() != null
            && txInfAndSts.getSplmtryData().getEnvlp().getCtrctRspsnList() != null
            && txInfAndSts.getSplmtryData().getEnvlp().getCtrctRspsnList().size() > 0) {
            List<String> senderContractIds = new ArrayList<>();
            for (CtrctRspsn ctrctRspsn : txInfAndSts.getSplmtryData().getEnvlp().getCtrctRspsnList()) {
                senderContractIds.add(ctrctRspsn.getCtrctId());
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
        if (txInfAndSts != null && txInfAndSts.getOrgnlTxRef() != null
                && txInfAndSts.getOrgnlTxRef().getDbtr() != null
                && txInfAndSts.getOrgnlTxRef().getDbtr().getPty() != null
                && txInfAndSts.getOrgnlTxRef().getDbtr().getPty().getNm() != null) {
            data.add(txInfAndSts.getOrgnlTxRef().getDbtr().getPty().getNm());
        }

        if (txInfAndSts != null && txInfAndSts.getOrgnlTxRef() != null
                && txInfAndSts.getOrgnlTxRef().getDbtrAcct() != null
                && txInfAndSts.getOrgnlTxRef().getDbtrAcct().getId() != null
                && txInfAndSts.getOrgnlTxRef().getDbtrAcct().getId().getOthr() != null
                && txInfAndSts.getOrgnlTxRef().getDbtrAcct().getId().getOthr().getId() != null) {
            data.add(txInfAndSts.getOrgnlTxRef().getDbtrAcct().getId().getOthr().getId());
        } 
        return data;
    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        // 按顺序赋值加解密处理后的敏感要素
        if (txInfAndSts != null && txInfAndSts.getOrgnlTxRef() != null 
                && txInfAndSts.getOrgnlTxRef().getDbtr() != null
                && txInfAndSts.getOrgnlTxRef().getDbtr().getPty() != null
                && txInfAndSts.getOrgnlTxRef().getDbtr().getPty().getNm() != null) {
            txInfAndSts.getOrgnlTxRef().getDbtr().getPty().setNm(encryptionFeatures.get(index++));
        }

        if (txInfAndSts != null && txInfAndSts.getOrgnlTxRef() != null
                && txInfAndSts.getOrgnlTxRef().getDbtrAcct() != null
                && txInfAndSts.getOrgnlTxRef().getDbtrAcct().getId() != null
                && txInfAndSts.getOrgnlTxRef().getDbtrAcct().getId().getOthr() != null
                && txInfAndSts.getOrgnlTxRef().getDbtrAcct().getId().getOthr().getId() != null) {
            txInfAndSts.getOrgnlTxRef().getDbtrAcct().getId().getOthr().setId(encryptionFeatures.get(index++));
        }
    }

    @Override
    public String clrSysWorkDt(){
        if (txInfAndSts != null && txInfAndSts.getOrgnlTxRef()!= null && txInfAndSts.getOrgnlTxRef().getUstrds() != null) {
            for (String ustrd : txInfAndSts.getOrgnlTxRef().getUstrds()){
                if (ustrd.contains("/SysWorkDt/")){
                    return ustrd.substring("/SysWorkDt/".length());
                }
            }
        }
        return null;
    }

    @Override
    public void fillSttlmDt(String sttlmDt) {
        if (txInfAndSts != null && txInfAndSts.getOrgnlTxRef() != null && txInfAndSts.getOrgnlTxRef().getUstrds() != null) {
            txInfAndSts.getOrgnlTxRef().getUstrds().add("/SttlmDt/" + sttlmDt);
        }
    }
}
