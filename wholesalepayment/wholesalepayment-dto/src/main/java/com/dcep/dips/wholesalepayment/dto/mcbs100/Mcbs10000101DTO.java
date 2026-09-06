package com.dcep.dips.wholesalepayment.dto.mcbs100;

import com.dcep.common.annotation.Gateway;
import com.dcep.gateway.mcbdc.dto.soap.McbsGwDTO;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * mcbs.100.001.01DTO
 * @Author luzhikuan
 * @date 2025-10-03 14:24:28
 */
@Data
@Gateway(msgTp = "mcbs.100.001.01")
@JacksonXmlRootElement(localName = "PayFXBatchBulkRsp")
public class Mcbs10000101DTO extends McbsGwDTO {
    /**
     * 组件GroupHeader
     */
    @JacksonXmlProperty(
            localName = "GrpHdr"
    )
    @NotNull
    @Valid
    private GrpHdr grpHdr;

    /**
     * 交易信息
     */
    @JacksonXmlProperty(
            localName = "TxInf"
    )
    @NotNull
    @Valid
    private NoInf noInf;

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

}
