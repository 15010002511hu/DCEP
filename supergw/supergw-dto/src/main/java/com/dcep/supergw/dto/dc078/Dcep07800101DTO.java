package com.dcep.supergw.dto.dc078;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Gateway(msgTp = "dcep.078.001.01", isReturn = true)
@JacksonXmlRootElement(localName = "CtrctMsgRcpt", namespace = "http://www.dcep.com/dcep/07800101/")
public class Dcep07800101DTO extends GwDTO implements Serializable {

    @NotNull
    @Valid
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @JacksonXmlProperty(localName = "GrpHdr")
    GrpHdr grpHdr;

    @NotNull
    @Valid
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    OrgnlGrpHdr orgnlGrpHdr;


    @NotNull
    @Valid
    @JacksonXmlProperty(localName = "RcptInf")
    RcptInf rcptInf;

    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader header) {
        return CheckUtils.responseMsgChk(header, this.grpHdr, this.orgnlGrpHdr);
    }
}
