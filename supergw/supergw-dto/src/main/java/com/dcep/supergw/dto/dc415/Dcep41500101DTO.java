package com.dcep.supergw.dto.dc415;

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
import com.dcep.supergw.validation.Check415Biz;
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
 * 8.3.6 商户及订单查询请求报文<dcep.415.001.01>
 * @author chenkai
 */
@JacksonXmlRootElement(localName = "OrdrQryReq",namespace = "http://www.dcep.com/dcep/41500101/")
@Getter
@Setter
@ToString
@Check415Biz(groups = Priority.Lowest.class)
@Gateway(msgTp = "dcep.415.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
public class Dcep41500101DTO extends GwDTO implements DataEncryption {

    private static final long serialVersionUID = -7231835772911811423L;


    /**
     * 【业务头组件】
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @Valid
    private GrpHdr grpHdr;

    /**
     * 【交易查询信息】
     */
    @JacksonXmlProperty(localName = "TrxInf")
    @NotNull
    @Valid
    private TrxInf trxInf;

    /**
     * 【付款钱包信息】
     */
    @JacksonXmlProperty(localName = "Dbtr")
    @Valid
    private Dbtr dbtr;

    /**
     * 【收款码信息】
     */
    @JacksonXmlProperty(localName = "QRCodeInf")
    @Valid
    private QRCodeInf qrcodeInf;
    
    
    /**
     * 【商户信息】
     * 1130版本新增by duz
     */
    @JacksonXmlProperty(localName = "MrchntInf")
    @Valid
    private MrchntInf mrchntInf;
    
    

    @Override
    public void init(){

    }

    @Override
    public String fetchMsgId(){
        return grpHdr.getMsgId();
    }

    @Override

	public boolean check(SoapHeader header) {
        return CheckUtils.requestMsgChk(header, grpHdr);
    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper){
        List<String> plainTextList = new ArrayList<>();
        if (dbtr != null) {
            //对明文加密
            if (dbtr.getDbtrWltId() != null) {
                plainTextList.add(dbtr.getDbtrWltId());
            }
        }
        List<String> cipherTextList = encryptionHelper.encrypt(plainTextList);
        if (dbtr != null) {
            if (dbtr.getDbtrWltId() != null) {
                dbtr.setDbtrWltId(cipherTextList.get(0));
            }
        }
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        List<String> cipherTextList = new ArrayList<>();
        if (dbtr != null) {
            //对明文加密
            if (dbtr.getDbtrWltId() != null) {
                cipherTextList.add(dbtr.getDbtrWltId());
            }
        }
        List<String> plainTextList = encryptionHelper.decrypt(cipherTextList);
        if (dbtr != null) {
            if (dbtr.getDbtrWltId() != null) {
                dbtr.setDbtrWltId(plainTextList.get(0));
            }
        }
    }
}
