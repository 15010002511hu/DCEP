package com.dcep.supergw.dto.dc031;

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
 * dcep.031.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-09-11 09:50:08
 */
@Data
@Gateway(
        msgTp = "dcep.031.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD)
)
@JacksonXmlRootElement(
        localName = "PdctModfyAudtDtlsQryReq",
        namespace = "http://www.dcep.com/dcep/03100101/"
)
public class Dcep03100101DTO extends GwDTO {
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
