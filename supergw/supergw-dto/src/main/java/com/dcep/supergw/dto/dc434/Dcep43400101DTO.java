package com.dcep.supergw.dto.dc434;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check434Biz1;
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

/**
 * @author : maxinyu
 * @version : Dcep43400101Dto.java v 0.1 2019-08-21
 * @description : 银行账户挂接管理应答报文<dcep.434.001.01>
 */
@JacksonXmlRootElement(localName = "PmtPtcMgmtRsp",namespace = "http://www.dcep.com/dcep/43400101/")
@Getter
@Setter
@ToString
@Gateway(msgTp ="dcep.434.001.01", isReturn = true)
@Check434Biz1(groups = Priority.Lowest.class)
public class Dcep43400101DTO extends GwDTO implements DataEncryption {
    private static final long serialVersionUID = 7761136704595953298L;

    /**
     * 业务头组件
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lower.class)
    @CheckGrpHdrMsgId(groups = Priority.Lower.class)
    @Valid
    private GrpHdr grpHdr;

    /**
     * 原报文主键组件
     */
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    @NotNull
    @Valid
    private OrgnlGrpHdr orgnlGrpHdr;

    /**
     * 业务信息
     */
    @JacksonXmlProperty(localName = "RspsnInf")
    @NotNull
    @Valid
    private RspsnInf rspsInf;

    /**
     * 签约人信息
     */
    @JacksonXmlProperty(localName = "SgnInf")
    @Valid
    private SgnInf sgnInf;

    /**
     * 钱包信息
     */
    @JacksonXmlProperty(localName = "WltInf")
    @Valid
    private WltInf wltInf;

    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }
    
    @Override
	public boolean check(SoapHeader header) {
        return CheckUtils.responseMsgChk(header, grpHdr, orgnlGrpHdr);
    }

    @Override
    public String fetchResultCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(rspsInf.getRspsnSts());
        if (StringUtils.isNotEmpty(rspsInf.getRjctCd())) {
            sb.append("-");
            sb.append(rspsInf.getRjctCd());
        }
        return sb.toString();
    }

    @Override
    public void encryptData(EncryptionHelper encryptHelper) {
        List<String> plainTextList =  fetchSecretFactor();
        List<String> cipherTextList =  encryptHelper.encrypt(plainTextList);
        secretAssign(cipherTextList);
    }

    @Override
    public void decryptData(EncryptionHelper encryptHelper) {
        List<String> cipherTextList =  fetchSecretFactor();
        List<String> plainTextList =  encryptHelper.decrypt(cipherTextList);
        secretAssign(plainTextList);
    }

    public List<String> fetchSecretFactor() {
        List<String> result = new ArrayList<>();
        if (this.sgnInf != null) {
            if (this.sgnInf.getSgnAcctId() != null) {
                result.add(this.sgnInf.getSgnAcctId());
            } else {
                result.add(null);
            }
            if (this.sgnInf.getSgnAcctNm() != null) {
                result.add(this.sgnInf.getSgnAcctNm());
            } else {
                result.add(null);
            }
        }else{
            result.add(null);
            result.add(null);
        }
        if (wltInf != null) {
            if (wltInf.getWltId() != null) {
                result.add(this.wltInf.getWltId());
            } else {
                result.add(null);
            }
        }
        return result;
    }

    public void secretAssign(List<String> secretList) {
        if (this.sgnInf != null) {
            if (this.sgnInf.getSgnAcctId() != null) {
                this.sgnInf.setSgnAcctId(secretList.get(0));
            }
            if (this.sgnInf.getSgnAcctNm() != null) {
                this.sgnInf.setSgnAcctNm(secretList.get(1));
            }
        }
        if (wltInf != null) {
            if (wltInf.getWltId() != null) {
                this.wltInf.setWltId(secretList.get(2));
            }
        }
    }


}
