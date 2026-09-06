package com.dcep.supergw.dto.dc030;

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
import com.dcep.supergw.validation.Check030Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * dcep.030.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-09-11 09:48:44
 */
@Data
@Gateway(
        msgTp = "dcep.030.001.01", isReturn = true
)
@JacksonXmlRootElement(
        localName = "PdctModfyAudtListQryRspn",
        namespace = "http://www.dcep.com/dcep/03000101/"
)
@Check030Biz(groups = Priority.Lowest.class)
public class Dcep03000101DTO extends GwDTO implements DataEncryption {
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
     * 应答分页信息
     */
    @JacksonXmlProperty(
            localName = "RspnPgInf"
    )
    @Valid
    private RspnPgInf rspnPgInf;

    /**
     * 合约产品修改审核记录列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "PdctModfyAudtRcrdList")
    @Valid
    private List<PdctModfyAudtRcrd> pdctModfyAudtRcrdList;

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
        if (this.pdctModfyAudtRcrdList != null && this.pdctModfyAudtRcrdList.size() > 0) {
            for (PdctModfyAudtRcrd rcrd : pdctModfyAudtRcrdList) {
                if (StringUtils.isNotBlank(rcrd.getCtctInf())) {
                    data.add(rcrd.getCtctInf());
                }
            }
        }

        return data;
    }

    private void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        if (this.pdctModfyAudtRcrdList != null && this.pdctModfyAudtRcrdList.size() > 0) {
            for (PdctModfyAudtRcrd rcrd : pdctModfyAudtRcrdList) {
                if (StringUtils.isNotBlank(rcrd.getCtctInf())) {
                    rcrd.setCtctInf(encryptionFeatures.get(index++));
                }
            }
        }
    }
}
