package com.dcep.supergw.dto.dc326;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check326Biz;
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

@JacksonXmlRootElement(localName = "AutoTopupStatQryRsp",
        namespace = "http://www.dcep.com/dcep/32600101/")
@Gateway(msgTp = "dcep.326.001.01", isReturn = true)
@Setter
@Getter
@ToString
@Check326Biz
public class Dcep32600101DTO extends GwDTO implements DataEncryption {

    private static final long serialVersionUID = -767063341329757886L;
    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "GrpHdr")
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    private GrpHdr grpHdr;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "BizQryRef")
    private BizQryRef bizQryRef;

    @Valid
    @JacksonXmlProperty(localName = "BizInf")
    private BizInf bizInf;

    @Valid
    @JacksonXmlProperty(localName = "OprlErr")
    private OprlErr oprlErr;

    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader header) {
        return true;
    }

    @Override
    public String fetchResultCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(bizQryRef.getQryRs());
        if (oprlErr != null) {
            if (StringUtils.isNotEmpty(oprlErr.getErr().getRjctCd())) {
                sb.append("-");
                sb.append(oprlErr.getErr().getRjctCd());
            }
        }
        return sb.toString();
    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        List<String> plainTextList = fetchSecretFactor();
        List<String> cipherTextList = encryptionHelper.encrypt(plainTextList);
        secretAssign(cipherTextList);
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        List<String> cipherTextList = fetchSecretFactor();
        List<String> plainTextList = encryptionHelper.decrypt(cipherTextList);
        secretAssign(plainTextList);
    }

    public List<String> fetchSecretFactor() {
        List<String> result = new ArrayList<>();
        if (this.bizInf != null) {
            if (bizInf.getWltId() != null) {
                result.add(bizInf.getWltId());
            } else {
                result.add(null);
            }
        }
        return result;
    }

    public void secretAssign(List<String> secretList) {
        if (this.bizInf != null) {
            if (bizInf.getWltId() != null) {
                bizInf.setWltId(secretList.get(0));
            }
        }
    }
}
