package com.dcep.supergw.dto.dc025;

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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * dcep.025.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:47:38
 */
@Data
@Gateway(
        msgTp = "dcep.025.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD)
)
@JacksonXmlRootElement(
        localName = "PdctCretAudtRcrdReq",
        namespace = "http://www.dcep.com/dcep/02500101/"
)
public class Dcep02500101DTO extends GwDTO {
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
     * 查询参数
     */
    @JacksonXmlProperty(
            localName = "QryParams"
    )
    @NotNull
    @Valid
    private QryParams qryParams;

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
