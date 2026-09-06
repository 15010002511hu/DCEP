package com.dcep.dips.wholesalepayment.dto.dc428;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * dcep.428.001.01DTO
 * @Author luteng
 * @date 2025-10-21 17:29:53
 */
@Data
@Gateway(
        msgTp = "dcep.428.001.01"
)
@JacksonXmlRootElement(
        localName = "RsltnOfInvstgtn",
        namespace = "http://www.dcep.com/dcep/42800101/"
)
public class Dcep42800101DTO extends GwDTO {
    /**
     * 组件Assignment
     */
    @JacksonXmlProperty(
            localName = "Assgnmt"
    )
    @NotNull
    @Valid
    private Assgnmt assgnmt;

    /**
     * 组件Status
     */
    @JacksonXmlProperty(
            localName = "Sts"
    )
    @NotNull
    @Valid
    private Sts sts;

    /**
     * 组件CancellationDetails
     */
    @JacksonXmlProperty(
            localName = "CxlDtls"
    )
    @NotNull
    @Valid
    private CxlDtls cxlDtls;

    @Override
    public void init() {
    }

    @Override
    public String fetchMsgId() {
        return null;
    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        return true;
    }
}
