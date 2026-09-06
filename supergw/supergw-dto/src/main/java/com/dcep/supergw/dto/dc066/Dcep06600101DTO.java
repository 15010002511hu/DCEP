package com.dcep.supergw.dto.dc066;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check066Biz;
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
 * 补贴预发放应答报文
 *
 * @Author qinchaoyong
 * @date 2025-03-03 10:35:41
 */
@Data
@Gateway(
        msgTp = "dcep.066.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD)
)
@JacksonXmlRootElement(
        localName = "SubPreDistResp",
        namespace = "http://www.dcep.com/dcep/06600101/"
)
@Check066Biz(groups = Priority.Lowest.class)
public class Dcep06600101DTO extends GwDTO implements DataEncryption {
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
     * 原报文信息
     */
    @JacksonXmlProperty(
            localName = "OrgnlGrpHdr"
    )
    @NotNull
    @Valid
    private OrgnlGrpHdr orgnlGrpHdr;

    /**
     * 组件ResponseInformation
     */
    @JacksonXmlProperty(
            localName = "RspnInf"
    )
    @NotNull
    @Valid
    private RspnInf rspnInf;

    @JacksonXmlProperty(
        localName = "BatchId"
    )
    @NotBlank
    @Length(min = 1,max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*",message = "禁止中文")
    private String batchId;

    /**
     * 补贴预发放列表
     */
    @JacksonXmlElementWrapper(
            useWrapping = false
    )
    @JacksonXmlProperty(
            localName = "SubPreDistList"
    )
    @Valid
    private List<SubPreDist> subPreDistList;

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
        if (this.subPreDistList != null) {
            for (SubPreDist subPreDist : this.subPreDistList) {
                if(StringUtils.isNotBlank(subPreDist.getPreDistCollMethod())){
                    data.add(subPreDist.getPreDistCollMethod());
                }

                if(StringUtils.isNotBlank(subPreDist.getWltId())){
                    data.add(subPreDist.getWltId());
                }
            }
        }

        return data;
    }

    private void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        if (this.subPreDistList != null) {
            for (SubPreDist subPreDist : subPreDistList) {
                if(StringUtils.isNotBlank(subPreDist.getPreDistCollMethod())){
                   subPreDist.setPreDistCollMethod(encryptionFeatures.get(index++));
                }

                if(StringUtils.isNotBlank(subPreDist.getWltId())){
                    subPreDist.setWltId(encryptionFeatures.get(index++));
                }
            }
        }

    }
}
