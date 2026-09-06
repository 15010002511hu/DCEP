package com.dcep.supergw.dto.dc028;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check028Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * dcep.028.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:52:03
 */
@Data
@Gateway(
        msgTp = "dcep.028.001.01", isReturn = true
)
@JacksonXmlRootElement(
        localName = "PdctAudtDtlQryRspn",
        namespace = "http://www.dcep.com/dcep/02800101/"
)
@Check028Biz(groups = Priority.Lowest.class)
public class Dcep02800101DTO extends GwDTO implements DataEncryption {
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
     * 原报文主键组件
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

    /**
     * 产品创建审核详情信息
     */
    @JacksonXmlProperty(
            localName = "PdctCretAudtDtlInf"
    )
    @Valid
    private PdctCretAudtDtlInf pdctCretAudtDtlInf;

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
        if (this.pdctCretAudtDtlInf != null && StringUtils.isNotBlank(this.pdctCretAudtDtlInf.getCtctInf())) {
            data.add(this.pdctCretAudtDtlInf.getCtctInf());
        }

        return data;
    }

    private void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        if (this.pdctCretAudtDtlInf != null && StringUtils.isNotBlank(this.pdctCretAudtDtlInf.getCtctInf())) {
            this.pdctCretAudtDtlInf.setCtctInf(encryptionFeatures.get(index++));
        }

    }
}
