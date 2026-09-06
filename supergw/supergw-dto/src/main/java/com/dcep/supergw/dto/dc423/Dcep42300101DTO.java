package com.dcep.supergw.dto.dc423;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * dcep.423.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2025-01-15 15:06:29
 */
@Data
@Gateway(msgTp = "dcep.423.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@JacksonXmlRootElement(
    localName = "GetUserSgnReq",
    namespace = "http://www.dcep.com/dcep/42300101/"
)
public class Dcep42300101DTO extends GwDTO {
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
     * 授权码信息
     */
    @JacksonXmlProperty(
        localName = "UserAuthCdInf"
    )
    @NotNull
    @Valid
    private UserAuthCdInf userAuthCdInf;

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
}
