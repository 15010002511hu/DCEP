package com.dcep.supergw.dto.dc425;

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
 * 补缴支付完成通知应答报文
 *
 * @Author qinchaoyong
 * @date 2025-03-17 16:22:49
 */
@Data
@Gateway(
        msgTp = "dcep.425.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD)
)
@JacksonXmlRootElement(
        localName = "ArrearsPdNtceReq",
        namespace = "http://www.dcep.com/dcep/42500101/"
)
public class Dcep42500101DTO extends GwDTO {
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
     * 补缴信息
     */
    @JacksonXmlProperty(
            localName = "PdInf"
    )
    @NotNull
    @Valid
    private PdInf pdInf;

    /**
     * 原交易信息
     */
    @JacksonXmlProperty(
            localName = "OrgnlTxInf"
    )
    @NotNull
    @Valid
    private OrgnlTxInf orgnlTxInf;

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
