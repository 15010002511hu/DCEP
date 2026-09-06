package com.dcep.dips.wholesalepayment.dto.mcbs102;

import com.dcep.common.annotation.Gateway;
import com.dcep.dips.wholesalepayment.dto.mcbs101.MsgHdr;
import com.dcep.gateway.mcbdc.dto.soap.McbsGwDTO;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Gateway(msgTp = "mcbs.102.001.01")
@JacksonXmlRootElement(localName = "PayFXBatchBulkRsp")
public class Mcbs10200101DTO extends McbsGwDTO {

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
     * 获取 msgId
     *
     * @return
     */
    @Override
    public String fetchMsgId() {
        return msgHdr.getMsgId();
    }

}
