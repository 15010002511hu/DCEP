package com.dcep.supergw.dto.dc075;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
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
@Gateway(msgTp = "dcep.075.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@JacksonXmlRootElement(localName = "CtrctStsSyncNotif", namespace = "http://www.dcep.com/dcep/07500101/")
public class Dcep07500101DTO extends GwDTO implements Serializable {

    @NotNull
    @Valid
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @JacksonXmlProperty(localName = "GrpHdr")
    GrpHdr grpHdr;

    @NotNull
    @Valid
    @JacksonXmlProperty(localName = "SyncInf")
    SyncInf syncInf;



    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader header) {
        return CheckUtils.requestMsgChk(header,grpHdr);
    }
}
