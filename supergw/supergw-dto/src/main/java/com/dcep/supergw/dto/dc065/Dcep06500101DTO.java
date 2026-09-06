package com.dcep.supergw.dto.dc065;

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
import com.dcep.supergw.validation.Check065Biz;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 补贴预发放申请报文
 *
 * @Author qinchaoyong
 * @date 2025-03-03 09:39:45
 */
@Data
@Gateway(
    msgTp = "dcep.065.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD)
)
@Check065Biz(groups = Priority.Lowest.class)
@JacksonXmlRootElement(
    localName = "SubPreDistReq",
    namespace = "http://www.dcep.com/dcep/06500101/"
)
public class Dcep06500101DTO extends GwDTO implements DataEncryption {

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
     * 活动开始时间
     */
    @JacksonXmlProperty(
        localName = "PreDistStartTm"
    )
    @JsonFormat(
        locale = "zh",
        timezone = "GMT+8",
        pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    @NotBlank
    @Pattern(
        regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})?",
        message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss"
    )
    private String preDistStartTm;

    /**
     * 活动结束时间
     */
    @JacksonXmlProperty(
        localName = "PreDistEndTm"
    )
    @JsonFormat(
        locale = "zh",
        timezone = "GMT+8",
        pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    @NotBlank
    @Pattern(
        regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})?",
        message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss"
    )
    private String preDistEndTm;

    /**
     * 补贴预发放列表
     */
    @JacksonXmlElementWrapper(
        useWrapping = false
    )
    @JacksonXmlProperty(
        localName = "SubPreDistList"
    )
    @NotNull
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
            for (SubPreDist subPreDist : subPreDistList) {
                if(StringUtils.isNotBlank(subPreDist.getCustIdCd())){
                    data.add(subPreDist.getCustIdCd());
                }

                if(StringUtils.isNotBlank(subPreDist.getCustNm())){
                    data.add(subPreDist.getCustNm());
                }

                if(StringUtils.isNotBlank(subPreDist.getPreDistCollMethod())){
                    data.add(subPreDist.getPreDistCollMethod());
                }
            }
        }

        return data;
    }

    private void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        if (this.subPreDistList != null) {
            for (SubPreDist subPreDist : subPreDistList) {
                if(StringUtils.isNotBlank(subPreDist.getCustIdCd())){
                    subPreDist.setCustIdCd(encryptionFeatures.get(index++));
                }

                if(StringUtils.isNotBlank(subPreDist.getCustNm())){
                    subPreDist.setCustNm(encryptionFeatures.get(index++));
                }

                if(StringUtils.isNotBlank(subPreDist.getPreDistCollMethod())){
                    subPreDist.setPreDistCollMethod(encryptionFeatures.get(index++));
                }
            }
        }

    }
}
