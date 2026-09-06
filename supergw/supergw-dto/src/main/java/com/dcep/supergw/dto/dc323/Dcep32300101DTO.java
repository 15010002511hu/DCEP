package com.dcep.supergw.dto.dc323;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check323Biz;
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
 * @author : weiqianjing
 * @version : Dcep32300101DTO.java
 * @description :订单确认请求报文
 */
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.323.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@JacksonXmlRootElement(localName = "OrdrConfReq", namespace = "http://www.dcep.com/dcep/32300101/")
@Check323Biz(groups = Priority.Lowest.class)
public class Dcep32300101DTO extends GwDTO implements DataEncryption {


    private static final long serialVersionUID = -5972924398578004761L;
    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "GrpHdr")
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    private GrpHdr grpHdr;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "TrxInf")
    private TrxInf trxInf;

    @Valid
    @JacksonXmlProperty(localName = "PmtChnlInf")
    private PmtChnlInf pmtChnlInf;


    @Valid
    @JacksonXmlProperty(localName = "UserInf")
    private UserInf userInf;

    @Valid
    @JacksonXmlProperty(localName = "UserTerInf")
    private UserTerInf userTerInf;

    @Valid
    @JacksonXmlProperty(localName = "QrCodeInf")
    private QrCodeInf qrCodeInf;

    @Valid
    @JacksonXmlProperty(localName = "PtcInf")
    private PtcInf ptcInf;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "CdtrInf")
    private CdtrInf cdtrInf;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "MrchntInf")
    private MrchntInf mrchntInf;

    @Valid
    @JacksonXmlProperty(localName = "OrdrInf")
    private OrdrInf ordrInf;

    @Valid
    @JacksonXmlProperty(localName = "PmtCodeInf")
    private PmtCodeInf pmtCodeInf;

    @Valid
    @JacksonXmlProperty(localName = "MrchntTerInf")
    private MrchntTerInf mrchntTerInf;


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
    public void encryptData(EncryptionHelper encryptHelper) {
        List<String> plainTextList = new ArrayList<>();
        if (cdtrInf != null) {
            //对明文加密
            if (cdtrInf.getCdtrWltId() != null) {
                plainTextList.add(cdtrInf.getCdtrWltId());
            }
        }
        List<String> cipherTextList = encryptHelper.encrypt(plainTextList);
        if (cdtrInf != null) {
            if (cdtrInf.getCdtrWltId() != null) {
                cdtrInf.setCdtrWltId(cipherTextList.get(0));
            }
        }
    }

    @Override
    public void decryptData(EncryptionHelper encryptHelper) {
        List<String> cipherTextList = new ArrayList<>();
        if (cdtrInf != null) {
            //对明文加密
            if (cdtrInf.getCdtrWltId() != null) {
                cipherTextList.add(cdtrInf.getCdtrWltId());
            }
        }
        List<String> plainTextList = encryptHelper.decrypt(cipherTextList);
        if (cdtrInf != null) {
            if (cdtrInf.getCdtrWltId() != null) {
                cdtrInf.setCdtrWltId(plainTextList.get(0));
            }
        }
    }
}
