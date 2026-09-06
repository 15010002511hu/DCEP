package com.dcep.supergw.dto.dc018;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check018Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * dcep.018.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:31:27
 */
@Data
@Gateway(
        msgTp = "dcep.018.001.01", isReturn = true
)
@JacksonXmlRootElement(
        localName = "PdctVrsnDtlRsp",
        namespace = "http://www.dcep.com/dcep/01800101/"
)
@Check018Biz(groups = Priority.Lowest.class)
public class Dcep01800101DTO extends GwDTO {
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
     * 原报文信息
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
     * 组件ProductVersionDetailInformation
     */
    @JacksonXmlProperty(
            localName = "PdctVrsnDtlInf"
    )
    @Valid
    private PdctVrsnDtlInf pdctVrsnDtlInf;

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
