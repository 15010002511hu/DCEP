package com.dcep.supergw.dto.dc067;

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
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

/**
 * 补贴资金发放请求报文
 *
 * @Author qinchaoyong
 * @date 2025-03-03 09:59:40
 */
@Data
@Gateway(
    msgTp = "dcep.067.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD)
)
@JacksonXmlRootElement(
    localName = "SubDistReq",
    namespace = "http://www.dcep.com/dcep/06700101/"
)
public class Dcep06700101DTO extends GwDTO implements DataEncryption {

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
     * 补贴批次ID
     */
    @JacksonXmlProperty(
        localName = "BatchId"
    )
    @Length(
        min = 1,
        max = 64
    )
    @NotBlank
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    private String batchId;

    /**
     * 发放流水号
     */
    @JacksonXmlProperty(
        localName = "TxId"
    )
    @Length(
        min = 1,
        max = 35
    )
    @NotBlank
    private String txId;

    /**
     * 补贴发放信息列表
     */
    @JacksonXmlElementWrapper(
        useWrapping = false
    )
    @JacksonXmlProperty(
        localName = "SubDistList"
    )
    @Valid
    private List<SubDist> subDistList;

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
        if (this.subDistList != null) {
            for (SubDist subDist : this.subDistList) {
                if (StringUtils.isNotBlank(subDist.getWltId())) {
                    data.add(subDist.getWltId());
                }
            }
        }

        return data;
    }

    private void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        if (this.subDistList != null) {
            for (SubDist subDist : this.subDistList) {
                if (StringUtils.isNotBlank(subDist.getWltId())) {
                    subDist.setWltId(encryptionFeatures.get(index++));
                }
            }
        }

    }
}
