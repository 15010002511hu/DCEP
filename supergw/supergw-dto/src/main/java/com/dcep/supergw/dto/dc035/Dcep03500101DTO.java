package com.dcep.supergw.dto.dc035;

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
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

/**
 * dcep.035.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-09-11 09:55:22
 */
@Data
@Gateway(
    msgTp = "dcep.035.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD)
)
@JacksonXmlRootElement(
    localName = "SgntrnInforChkReq",
    namespace = "http://www.dcep.com/dcep/03500101/"
)
public class Dcep03500101DTO extends GwDTO implements DataEncryption {

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
     * 合约产品ID
     */
    @JacksonXmlProperty(
        localName = "PdctId"
    )
    @Length(
        min = 1,
        max = 16
    )
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    private String pdctId;

    /**
     * 组件QueryParameter
     */
    @JacksonXmlProperty(
        localName = "QryParam"
    )
    @NotNull
    @Valid
    private QryParam qryParam;

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
        if (this.qryParam != null) {
            if (this.qryParam.getSgntrPtyList() != null || !this.qryParam.getSgntrPtyList().isEmpty()) {
                for (SgntrPty sgntrPty : qryParam.getSgntrPtyList()) {
                    if (sgntrPty.getSgntrWltInf() != null) {
                        if (StringUtils.isNotBlank(sgntrPty.getSgntrWltInf().getCstmrNm())) {
                            data.add(sgntrPty.getSgntrWltInf().getCstmrNm());
                        }
                        if (StringUtils.isNotBlank(sgntrPty.getSgntrWltInf().getWltId())) {
                            data.add(sgntrPty.getSgntrWltInf().getWltId());
                        }
                    }
                }
            }
        }

        return data;
    }

    private void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        if (this.qryParam != null) {
            if (this.qryParam.getSgntrPtyList() != null || !this.qryParam.getSgntrPtyList().isEmpty()) {
                for (SgntrPty sgntrPty : qryParam.getSgntrPtyList()) {
                    if (sgntrPty.getSgntrWltInf() != null) {
                        if (StringUtils.isNotBlank(sgntrPty.getSgntrWltInf().getCstmrNm())) {
                            sgntrPty.getSgntrWltInf().setCstmrNm(encryptionFeatures.get(index++));
                        }
                        if (StringUtils.isNotBlank(sgntrPty.getSgntrWltInf().getWltId())) {
                            sgntrPty.getSgntrWltInf().setWltId(encryptionFeatures.get(index++));
                        }
                    }
                }
            }
        }

    }
}
