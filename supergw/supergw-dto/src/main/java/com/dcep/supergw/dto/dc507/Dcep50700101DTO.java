package com.dcep.supergw.dto.dc507;

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
import com.dcep.supergw.validation.Check507Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 子钱包推送商户请求报文<dcep.507.001.01>
 * 子钱包推送是指将DC/EP支付能力推送至商户，以达到在商户的APP上使用DC/EP支付能力的目的。该报文功能是针对付款用户和商户不在同一运营机构的场景。
 *
 * @author DUZHONG
 */
@JacksonXmlRootElement(localName = "ChdWltPushReq", namespace = "http://www.dcep.com/dcep/50700101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.507.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@Check507Biz
public class Dcep50700101DTO extends GwDTO implements DataEncryption {
    private static final long serialVersionUID = -3981843314287162376L;

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
     * 【管理类型】 MT01：身份认证 MT02：身份确认
     */
    @JacksonXmlProperty(localName = "MgmtTp")
    @Pattern(regexp = "MT01||MT02")
    @NotBlank(groups = Priority.Highest.class)
    private String mgmtTp;

    /**
     * 【协议签约信息】
     */
    @JacksonXmlProperty(localName = "PtcInf")
    @NotNull
    @Valid
    private PtcInf ptcInf;

    /**
     * 【商户信息】
     */
    @JacksonXmlProperty(localName = "MrchntInf")
    @NotNull
    @Valid
    private MrchntInf mrchntInf;

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
        if (this.ptcInf != null) {
            if (ptcInf.getMobPhNo() != null) {
                result.add(ptcInf.getMobPhNo());
            } else {
                result.add(null);
            }
        }
        return result;
    }

    public void secretAssign(List<String> secretList) {
        if (this.ptcInf != null) {
            if (ptcInf.getMobPhNo() != null) {
                ptcInf.setMobPhNo(secretList.get(0));
            }
        }
    }
}