package com.dcep.supergw.dto.dc361;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.CheckAccountTag;
import com.dcep.common.annotation.CheckAccountTag.Type;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author : maxinyu
 * @version : Dcep36100101DTO.java v 0.1 2023-01-05 Copyright 2023 PBCDCI ALL Rights
 * @description : 订单支付请求报文.用于支付App主扫场景或展示数字人民币付款码时被扫非免密支付场景
 */
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.361.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@JacksonXmlRootElement(localName = "OrdrPmtReq", namespace = "http://www.dcep.com/dcep/36100101/")
@CheckAccountTag(type = Type.WID, path = {"cdtrInf.cdtrWltId"})
public class Dcep36100101DTO extends GwDTO implements DataEncryption {
    private static final long serialVersionUID = 578394023421369655L;

    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull
    @Valid
    GrpHdr grpHdr;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    private OrgnlGrpHdr orgnlGrpHdr;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "TrxInf")
    private TrxInf trxInf;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "CdtrInf")
    private CdtrInf cdtrInf;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "MrchntInf")
    private MrchntInf mrchntInf;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "OrdrInf")
    private OrdrInf ordrInf;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "PtcInf")
    private PtcInf ptcInf;

    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader header) {
        return CheckUtils.requestMsgChk(header, grpHdr);
    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        List<String> plainTextList = new ArrayList<>();
        if (cdtrInf != null) {
            // 对明文加密
            if (cdtrInf.getCdtrWltId() != null) {
                plainTextList.add(cdtrInf.getCdtrWltId());
            }
        }
        List<String> cipherTextList = encryptionHelper.encrypt(plainTextList);
        if (cdtrInf != null) {
            if (cdtrInf.getCdtrWltId() != null) {
                cdtrInf.setCdtrWltId(cipherTextList.get(0));
            }
        }
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        List<String> cipherTextList = new ArrayList<>();
        if (cdtrInf != null) {
            // 对明文加密
            if (cdtrInf.getCdtrWltId() != null) {
                cipherTextList.add(cdtrInf.getCdtrWltId());
            }
        }
        List<String> plainTextList = encryptionHelper.decrypt(cipherTextList);
        if (cdtrInf != null) {
            if (cdtrInf.getCdtrWltId() != null) {
                cdtrInf.setCdtrWltId(plainTextList.get(0));
            }
        }
    }

}
