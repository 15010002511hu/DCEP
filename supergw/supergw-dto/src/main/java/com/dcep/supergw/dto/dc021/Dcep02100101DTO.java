package com.dcep.supergw.dto.dc021;

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
 * dcep.021.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:34:50
 */
@Data
@Gateway(
        msgTp = "dcep.021.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD)
)
@JacksonXmlRootElement(
        localName = "PdctVrsnArrgmtReq",
        namespace = "http://www.dcep.com/dcep/02100101/"
)
public class Dcep02100101DTO extends GwDTO {
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
     * 提审请求信息
     */
    @JacksonXmlProperty(
            localName = "AudtInf"
    )
    @NotNull
    @Valid
    private AudtInf audtInf;

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
