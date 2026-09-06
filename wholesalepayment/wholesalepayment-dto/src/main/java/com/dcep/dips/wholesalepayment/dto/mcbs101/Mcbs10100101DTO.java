package com.dcep.dips.wholesalepayment.dto.mcbs101;

import com.dcep.common.annotation.Gateway;
import com.dcep.gateway.mcbdc.dto.soap.McbsGwDTO;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * mcbs.101.001.01DTO
 * @Author luzhikuan
 * @date 2025-10-03 14:53:28
 */
@Data
@Gateway(msgTp = "mcbs.101.001.01")
@JacksonXmlRootElement(localName = "PayFXBatchBulkRsp")
public class Mcbs10100101DTO extends McbsGwDTO {
    /**
     * 组件GroupHeader
     */
    @JacksonXmlProperty(
            localName = "MsgHdr"
    )
    @NotNull
    @Valid
    private MsgHdr msgHdr;

    /**
     * 交易信息
     */
    @JacksonXmlProperty(
            localName = "OrgnlGrpInf"
    )
    @NotNull
    @Valid
    private OrgnlGrpInf orgnlGrpInf;

    /**
     * 清零信息
     */
    @JacksonXmlProperty(
            localName = "ClrDtls"
    )
    @NotNull
    @Valid
    private ClrDtls clrDtls;

    @Override
    public String fetchMsgId() {
        return msgHdr.getMsgId();
    }

}
