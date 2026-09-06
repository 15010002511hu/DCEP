/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc213;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.*;
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
import com.dcep.dips.wholesalepayment.constants.Common;
import com.dcep.dips.wholesalepayment.constants.Constant;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.DtoCheckUtil;
import com.dcep.dips.wholesalepayment.validation.CheckClrBatId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author cxf
 * @version $Id: Dcep21301001DTO.java
 */
@JacksonXmlRootElement(localName = "FIToFICstmrDrctDbt", namespace = "http://www.dcep.com/dcep/21301001/")
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Gateway(msgTp = "dcep.213.010.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {
        @RpcInfo(name = Constant.NAME_DCEPSND, methods = { @GwMethod(name = Constant.METHOD_MBRIDGE_PREPARE) }) }))
@Clearing(presumeStatus = ClearingStatusEnum.PRESUME_FAILED, action = ClearingActionEnum.PREPARE, confirmTimeout = 300,
        checkMode = ClearingCheckModeEnum.PAYEE_CHECK, cdtDbtInd = ClearingProdCdtDbtIndEnum.DBIT, actgBizTp = ActgBizTpEnum.SWBC05)
@Record(saveMode = RecordSaveModeEnum.ALL)
@CheckClrBatId
@CheckBizCode(bizTypeCode = "drctDbtTxInf.pmtTpInf.lclInstrm.prtry", bizCtgyCode = "drctDbtTxInf.purp.prtry")
public class Dcep21301001DTO extends GwDTO implements ClearingDTO, DataEncryption {

    /**  */
    private static final long serialVersionUID = -1536295749998288670L;

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
     * 货币桥信息
     */
    @JsonIgnore
    private MbridgeReqDTO mbridgeReqDTO;

    /**
     * @see GwDTO#init()
     */
    @Override
    public void init() {
        rmtInf(this.drctDbtTxInf.getUstrds());
    }

    /**
     * 将ustrds附言字段的值赋值到RmtInf对象
     * 
     * @param ustrds
     */
    void rmtInf(List<String> ustrds) {
        if (ustrds != null && !ustrds.isEmpty()) {
            this.getDrctDbtTxInf().setRmtInf(new RmtInf(ustrds));
        }
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
        // 向报文中赋交易批次号
        drctDbtTxInf.getPmtId().setInstrId(response.getResult().getBatchId());
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
        
        // 货币桥业务类型，货币桥附加数据域必输
        RmtInf rmtInf = this.getDrctDbtTxInf().getRmtInf();
        String bizTp = this.getDrctDbtTxInf().getPmtTpInf().getLclInstrm().getPrtry();
        if (Common.MBRIDGE_PURP_DBTR.equals(bizTp) || Common.MBRIDGE_PURP_NEW.equals(bizTp)
                || Common.GCSC_MBRIDGE_PURP.equals(bizTp)) {
            if (rmtInf == null || (rmtInf != null && (StringUtils.isBlank(rmtInf.getReason())
                    || StringUtils.isBlank(rmtInf.getParameterId()) || StringUtils.isBlank(rmtInf.getSndChnlSys())
                    || StringUtils.isBlank(rmtInf.getRcvChnlSys())))) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "业务类型为货币桥时，货币桥附加数据域必填");
            }
            
            if (rmtInf.getSndChnlSys().equals(rmtInf.getRcvChnlSys())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "发起接收渠道系统标识非法");
            }
        }

        // 校验附言Ustrds里标签字段
        ValidateUtils.validate(this.getDrctDbtTxInf().getRmtInf());

        return DtoCheckUtil.checkPayeeMsgInst(soapHeader.getSender(), this.clrDbtrPtyId(), soapHeader.getReceiver(),
                this.clrCdtrPtyId());

    }

    @Override
    public com.dcep.common.model.soap.OrgnlGrpHdr presumeConfirm() {
        com.dcep.common.model.soap.OrgnlGrpHdr orgnlGrpHdr = new com.dcep.common.model.soap.OrgnlGrpHdr();
        orgnlGrpHdr.setOrgnlInstgPty(this.drctDbtTxInf.getCdtrAgt().getFinInstnId().getClrSysMmbId().getMmbId());
        orgnlGrpHdr.setOrgnlMsgId(this.getGrpHdr().getMsgId());
        orgnlGrpHdr.setOrgnlMT(MsgTpEnum.DBT_REQUEST_ASYN.getCode());
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
        return MsgTpEnum.DBT_REQUEST_ASYN.getCode();
    }

    @Override
    public String recMsgId() {
        return grpHdr.getMsgId();
    }

//    @Override
//    public String recDbtrWltId() {
//        return drctDbtTxInf.getDbtrAcct() != null ? drctDbtTxInf.getDbtrAcct().getId().getOthr().getId() : null;
//    }
//
//    @Override
//    public String recCdtrWltId() {
//        return drctDbtTxInf.getCdtrAcct().getId().getOthr().getId();
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
        return MsgTpEnum.DBT_REQUEST_ASYN.getCode();
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
    public String clrDbtrWltId() {
        return drctDbtTxInf.getDbtrAcct().getId().getOthr().getId();
    }

    @Override
    public String clrDbtrSysId() {
        // 接收渠道
        if (drctDbtTxInf.getRmtInf() != null) {
            return drctDbtTxInf.getRmtInf().getRcvChnlSys();
        }
        return null;
    }

    @Override
    public String clrCdtrPtyId() {
        return drctDbtTxInf.getCdtrAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String clrCdtrWltId() {
        return drctDbtTxInf.getCdtrAcct().getId().getOthr().getId();
    }

    @Override
    public String clrCdtrSysId() {
        // 发起渠道
        if (drctDbtTxInf.getRmtInf() != null) {
            return drctDbtTxInf.getRmtInf().getSndChnlSys();
        }
        return null;
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
        // 交易描述信息(付款人钱包ID)
        return drctDbtTxInf.getDbtrAcct() != null ? drctDbtTxInf.getDbtrAcct().getId().getOthr().getId() : null;
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
        // 发起渠道
        if (drctDbtTxInf.getRmtInf() != null) {
            return drctDbtTxInf.getRmtInf().getSndChnlSys();
        }
        return null;
    }

    @Override
    public String clrRecvPtyId() {
        return drctDbtTxInf.getDbtrAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    @Override
    public String clrRecvSysId() {
        // 接收渠道
        if (drctDbtTxInf.getRmtInf() != null) {
            return drctDbtTxInf.getRmtInf().getRcvChnlSys();
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


//    @Override
//    public String clrSendInst() {
//        return drctDbtTxInf.getCdtrAgt().getFinInstnId().getClrSysMmbId().getMmbId();
//    }

    @Override
    public String clrPresumeTm() {
        return null;
    }

    @Override
    public String clrAcctTp() {
        return ClrAcctTpEnum.PAY.getCode();
    }

    @Override
    public String clrSndChnlSys() {
        // 发起渠道
        if (drctDbtTxInf.getRmtInf() != null) {
            return drctDbtTxInf.getRmtInf().getSndChnlSys();
        }
        return null;
    }

    @Override
    public String clrRcvChnlSys() {
        // 接收渠道
        if (drctDbtTxInf.getRmtInf() != null) {
            return drctDbtTxInf.getRmtInf().getRcvChnlSys();
        }
        return null;
    }

    @Override
    public MbridgeReqDTO clrMbridgeInf() {
        return mbridgeReqDTO;
    }

    @Override
    public void fillMsgId(String msgId, String orgMsgId) {
        // 赋值报文标识号
        grpHdr.setMsgId(msgId);
        drctDbtTxInf.getPmtId().setEndToEndId(msgId);
        drctDbtTxInf.getPmtId().setTxId(msgId);
    }

    public List<String> fetchEncryptionFeatures() {
        // 获取需加解密处理的敏感要素，若为空则赋值为null，保证前后顺序一致
        List<String> data = new ArrayList<>();
        if (drctDbtTxInf != null && drctDbtTxInf.getDbtr() != null
                && drctDbtTxInf.getDbtr().getNm() != null) {
            data.add(drctDbtTxInf.getDbtr().getNm());
        }

        if (drctDbtTxInf != null && drctDbtTxInf.getDbtrAcct() != null
                && drctDbtTxInf.getDbtrAcct().getId() != null
                && drctDbtTxInf.getDbtrAcct().getId().getOthr() != null
                && drctDbtTxInf.getDbtrAcct().getId().getOthr().getId() != null) {
            data.add(drctDbtTxInf.getDbtrAcct().getId().getOthr().getId());
        }

        if (drctDbtTxInf != null && drctDbtTxInf.getCdtr() != null
                && drctDbtTxInf.getCdtr().getNm() != null) {
            data.add(drctDbtTxInf.getCdtr().getNm());
        }

        if (drctDbtTxInf != null && drctDbtTxInf.getCdtrAcct() != null
                && drctDbtTxInf.getCdtrAcct().getId() != null
                && drctDbtTxInf.getCdtrAcct().getId().getOthr() != null
                && drctDbtTxInf.getCdtrAcct().getId().getOthr().getId() != null) {
            data.add(drctDbtTxInf.getCdtrAcct().getId().getOthr().getId());
        }

        return data;
    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        // 按顺序赋值加解密处理后的敏感要素
        if (drctDbtTxInf != null && drctDbtTxInf.getDbtr() != null
                && drctDbtTxInf.getDbtr().getNm() != null) {
            drctDbtTxInf.getDbtr().setNm(encryptionFeatures.get(index++));
        }

        if (drctDbtTxInf != null && drctDbtTxInf.getDbtrAcct() != null 
                && drctDbtTxInf.getDbtrAcct().getId() != null
                && drctDbtTxInf.getDbtrAcct().getId().getOthr() != null
                && drctDbtTxInf.getDbtrAcct().getId().getOthr().getId() != null) {
            drctDbtTxInf.getDbtrAcct().getId().getOthr().setId(encryptionFeatures.get(index++));
        }

        if (drctDbtTxInf != null && drctDbtTxInf.getCdtr() != null
                && drctDbtTxInf.getCdtr().getNm() != null) {
            drctDbtTxInf.getCdtr().setNm(encryptionFeatures.get(index++));
        }

        if (drctDbtTxInf != null && drctDbtTxInf.getCdtrAcct() != null 
                && drctDbtTxInf.getCdtrAcct().getId() != null
                && drctDbtTxInf.getCdtrAcct().getId().getOthr() != null
                && drctDbtTxInf.getCdtrAcct().getId().getOthr().getId() != null) {
            drctDbtTxInf.getCdtrAcct().getId().getOthr().setId(encryptionFeatures.get(index++));
        }
    }

}
