/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc381;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.CheckBizCode;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

/**
 * 金额锁定请求报文
 * 
 * @author laimincai
 * @date 2023/12/29
 */
@Gateway(msgTp = "dcep.381.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@CheckBizCode(bizCtgyCode = "trxInf.trxCtgyPurpCd", bizTypeCode = "trxInf.trxBizTp")
@JacksonXmlRootElement(localName = "ApplyBalanceLockReq", namespace = "http://www.dcep.com/dcep/38100101/")
public class Dcep38100101DTO extends GwDTO implements DataEncryption {

    private static final long serialVersionUID = 1L;
    
    /*
     * 业务头组件
     */
    @Valid
    @NotNull
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @JacksonXmlProperty(localName = "GrpHdr")
    private GrpHdr grpHdr;
    
    /*
     * 交易信息
     */
    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "TrxInf")
    private TrxInf trxInf;
    
    /*
     * 锁定钱包信息
     */
    @Valid
    @JacksonXmlProperty(localName = "WltInf")
    private WltInf wltInf;
    
    /*
     * APDU信息
     */
    @Valid
    @JacksonXmlProperty(localName = "APDUInfo")
    private APDUInfo apduInfo;
    
    /*
     * 商户信息
     */
    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "MrchntInf")
    private MrchntInf mrchntInf;

    /*
     * 二级商户信息
     */
    @Valid
    @JacksonXmlProperty(localName = "SubMrchntInf")
    private SubMrchntInf subMrchntInf;
    
    @Override
    public void init() {
    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader header) { 
        if (trxInf != null && StringUtils.equals(trxInf.getTrxTp(), "TT19") && apduInfo == null) {
            throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "交易类型TT19-硬钱包金额锁定支付时, 硬钱包APDU信息必填");
        }
        
        return CheckUtils.requestMsgChk(header, grpHdr);
    }

    public GrpHdr getGrpHdr() {
        return grpHdr;
    }

    public void setGrpHdr(GrpHdr grpHdr) {
        this.grpHdr = grpHdr;
    }

    public TrxInf getTrxInf() {
        return trxInf;
    }

    public void setTrxInf(TrxInf trxInf) {
        this.trxInf = trxInf;
    }

    public WltInf getWltInf() {
        return wltInf;
    }

    public void setWltInf(WltInf wltInf) {
        this.wltInf = wltInf;
    }

    public APDUInfo getApduInfo() {
        return apduInfo;
    }

    public void setApduInfo(APDUInfo apduInfo) {
        this.apduInfo = apduInfo;
    }

    public MrchntInf getMrchntInf() {
        return mrchntInf;
    }

    public void setMrchntInf(MrchntInf mrchntInf) {
        this.mrchntInf = mrchntInf;
    }

    public SubMrchntInf getSubMrchntInf() {
        return subMrchntInf;
    }

    public void setSubMrchntInf(SubMrchntInf subMrchntInf) {
        this.subMrchntInf = subMrchntInf;
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
        if (null != wltInf && null != wltInf.getWltId()) {
            data.add(wltInf.getWltId());
        }
        return data;
    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        // 按顺序赋值加解密处理后的敏感要素
        if (null != wltInf && null != wltInf.getWltId()) {
            wltInf.setWltId(encryptionFeatures.get(0));
        }
    }

    @Override
    public String toString() {
        return "Dcep38100101DTO [grpHdr=" + grpHdr + ", trxInf=" + trxInf + ", wltInf=" + wltInf + ", apduInfo="
                + apduInfo + ", mrchntInf=" + mrchntInf + ", subMrchntInf=" + subMrchntInf + "]";
    }
}
