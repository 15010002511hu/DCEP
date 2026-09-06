package com.dcep.supergw.dto.dc426;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check426Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 补缴支付完成通知报文
 *
 * @Author qinchaoyong
 * @date 2025-03-17 16:23:29
 */
@Data
@Gateway(
        msgTp = "dcep.426.001.01", isReturn = true
)
@JacksonXmlRootElement(
        localName = "ArrearsPdNtceRsp",
        namespace = "http://www.dcep.com/dcep/42600101/"
)
@Check426Biz(groups = Priority.Lowest.class)
public class Dcep42600101DTO extends GwDTO {
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
     * 原报文主键组件
     */
    @JacksonXmlProperty(
            localName = "OrgnlGrpHdr"
    )
    @NotNull
    @Valid
    private OrgnlGrpHdr orgnlGrpHdr;

    /**
     * 组件ResponsionInformation
     */
    @JacksonXmlProperty(
            localName = "RspsnInf"
    )
    @NotNull
    @Valid
    private RspsnInf rspsnInf;

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
