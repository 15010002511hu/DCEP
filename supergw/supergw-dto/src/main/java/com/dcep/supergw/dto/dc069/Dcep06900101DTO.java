package com.dcep.supergw.dto.dc069;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.encryption.TransEncryption;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check069Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Check069Biz
@Gateway(
    msgTp = "dcep.069.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD)
)
@JacksonXmlRootElement(
    localName = "CtrctSgnNtc",
    namespace = "http://www.dcep.com/dcep/06900101/"
)
public class Dcep06900101DTO extends GwDTO implements Serializable, TransEncryption {

    @NotNull
    @Valid
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @JacksonXmlProperty(localName = "GrpHdr")
    GrpHdr grpHdr;

    @NotNull
    @Valid
    @JacksonXmlProperty(localName = "NtcInf")
    NtcInf ntcInf;


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
    public void transEncrypt(EncryptionHelper encryptionHelper) {
        if (this.ntcInf != null) {
            if (ntcInf.argmtTxt != null) {
                if (StringUtils.isNotEmpty(ntcInf.argmtTxt.encTrsKey)) {
                    ntcInf.argmtTxt.setEncTrsKey(encryptionHelper.encrypt(ntcInf.argmtTxt.encTrsKey));
                }
            }
        }
    }
}
