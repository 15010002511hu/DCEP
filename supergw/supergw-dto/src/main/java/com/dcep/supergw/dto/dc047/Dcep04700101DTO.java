package com.dcep.supergw.dto.dc047;

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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

/**
 * dcep.047.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:37:34
 */
@Data
@Gateway(
        msgTp = "dcep.047.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD)
)
@JacksonXmlRootElement(
        localName = "CtrctChngCnfrm",
        namespace = "http://www.dcep.com/dcep/04700101/"
)
public class Dcep04700101DTO extends GwDTO implements DataEncryption {
    /**
     * 业务头组件
     */
    @JacksonXmlProperty(
            localName = "GrpHdr"
    )
    @NotNull
    @Valid
    @CheckGrpHdrOrgId(
            groups = Priority.Lowest.class
    )
    @CheckGrpHdrMsgId(
            groups = Priority.Lowest.class
    )
    private GrpHdr grpHdr;

    /**
     * 合约变更记录Id
     */
    @JacksonXmlProperty(
            localName = "CtrctChngRcrdId"
    )
    @Length(
            min = 1,
            max = 64
    )
    @NotBlank
    private String ctrctChngRcrdId;

    /**
     * 合约运营机构
     */
    @JacksonXmlProperty(
            localName = "OprInstnId"
    )
    @Length(
            min = 1,
            max = 14
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String oprInstnId;

    /**
     * 签约方运营机构
     */
    @JacksonXmlProperty(
            localName = "CtrctPtyId"
    )
    @Length(
            min = 1,
            max = 14
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String ctrctPtyId;

    /**
     * 签约方列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "CtrctPtyList")
    @NotNull
    @Size(min = 2, max = 99)
    @Valid
    private List<CtrctPty> ctrctPtyList;

    @Override
    public void init() {
    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        return true;
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

    private List<String> fetchEncryptionFeatures() {
        List<String> data = new ArrayList();
        if (this.ctrctPtyList != null && this.ctrctPtyList.size() > 0) {
            for (CtrctPty ctrctPty : this.ctrctPtyList) {
                if (ctrctPty != null && ctrctPty.getWltInf() != null) {
                    WltInf wltInf = ctrctPty.getWltInf();
                    if (StringUtils.isNotBlank(wltInf.getCstmrNm())) {
                        data.add(wltInf.getCstmrNm());
                    }

                    if (StringUtils.isNotBlank(wltInf.getWltId())) {
                        data.add(wltInf.getWltId());
                    }
                }
            }

        }

        return data;
    }

    private void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        if (this.ctrctPtyList != null && this.ctrctPtyList.size() > 0) {
            for (CtrctPty ctrctPty : this.ctrctPtyList) {
                if (ctrctPty != null && ctrctPty.getWltInf() != null) {
                    WltInf wltInf = ctrctPty.getWltInf();
                    if (StringUtils.isNotBlank(wltInf.getCstmrNm())) {
                        wltInf.setCstmrNm(encryptionFeatures.get(index++));
                    }

                    if (StringUtils.isNotBlank(wltInf.getWltId())) {
                        wltInf.setWltId(encryptionFeatures.get(index++));
                    }
                }
            }
        }
    }
}
