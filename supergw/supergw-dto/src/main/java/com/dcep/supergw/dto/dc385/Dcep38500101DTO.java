/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc385;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 金额解锁请求
 * 
 * @author laimincai
 * @date 2023/12/29
 */
@Gateway(msgTp = "dcep.385.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@JacksonXmlRootElement(localName = "ApplyUnLockReq", namespace = "http://www.dcep.com/dcep/38500101/")
public class Dcep38500101DTO extends GwDTO implements DataEncryption {

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
     * 原报文主键组件
     */
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    @NotNull
    @Valid
    private OrgnlGrpHdr orgnlGrpHdr;
    
    @Override
    public void init() {
    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader header) {
        return true;
    }

    public GrpHdr getGrpHdr() {
        return grpHdr;
    }

    public void setGrpHdr(GrpHdr grpHdr) {
        this.grpHdr = grpHdr;
    }

    public OrgnlGrpHdr getOrgnlGrpHdr() {
        return orgnlGrpHdr;
    }

    public void setOrgnlGrpHdr(OrgnlGrpHdr orgnlGrpHdr) {
        this.orgnlGrpHdr = orgnlGrpHdr;
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
        if (null != orgnlGrpHdr && orgnlGrpHdr.getOrgnlWltInf() != null 
                && null != orgnlGrpHdr.getOrgnlWltInf().getWltId()) {
            data.add(orgnlGrpHdr.getOrgnlWltInf().getWltId());
        }
        return data;
    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        // 按顺序赋值加解密处理后的敏感要素
        if (null != orgnlGrpHdr && orgnlGrpHdr.getOrgnlWltInf() != null 
                && null != orgnlGrpHdr.getOrgnlWltInf().getWltId()) {
            orgnlGrpHdr.getOrgnlWltInf().setWltId(encryptionFeatures.get(0));
        }
    }

    @Override
    public String toString() {
        return "Dcep38500101DTO [grpHdr=" + grpHdr + ", orgnlGrpHdr=" + orgnlGrpHdr + "]";
    }
}
