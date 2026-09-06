package com.dcep.supergw.dto.dc535;

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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JacksonXmlRootElement(localName = "BankCardInfQryReq", namespace = "http://www.dcep.com/dcep/53500101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.535.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
public class Dcep53500101DTO extends GwDTO implements DataEncryption {
    /**
     *
     */
    private static final long serialVersionUID = 4248639192992850410L;

    /**
     * 业务头组件
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @Valid
    private GrpHdr grpHdr;

    /**
     * 银行卡信息
     */
    @JacksonXmlProperty(localName = "CardInf")
    @NotNull
    @Valid
    private CardInf cardInf;

    /**
     * 【协议信息】
     */
    @JacksonXmlProperty(localName = "WltInf")
    @NotNull
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
        return CheckUtils.requestMsgChk(header, grpHdr);
    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        List<String> plainTextList =  fetchSecretFactor();
        List<String> cipherTextList =  encryptionHelper.encrypt(plainTextList);
        secretAssign(cipherTextList);
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        List<String> cipherTextList =  fetchSecretFactor();
        List<String> plainTextList =  encryptionHelper.decrypt(cipherTextList);
        secretAssign(plainTextList);
    }

    public List<String> fetchSecretFactor() {
        List<String> result = new ArrayList<>();
        if (this.cardInf != null) {
            if (cardInf.getSgnAcctId() != null) {
                result.add(cardInf.getSgnAcctId());
            } else {
                result.add(null);
            }
        }else{
            result.add(null);
        }
        if (this.wltInf != null) {
            if (wltInf.getWltId() != null) {
                result.add(wltInf.getWltId());
            } else {
                result.add(null);
            }
        }
        return result;
    }

    public void secretAssign(List<String> secretList) {
        if (this.cardInf != null) {
            if (cardInf.getSgnAcctId() != null) {
                cardInf.setSgnAcctId(secretList.get(0));
            }
        }
        if (this.wltInf != null) {
            if (wltInf.getWltId() != null) {
                wltInf.setWltId(secretList.get(1));
            }
        }
    }
}
