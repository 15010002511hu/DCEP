package com.dcep.supergw.dto.dc421;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * dcep.421.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:26:50
 */
@Data
@Gateway(
        msgTp = "dcep.421.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD)
)
@JacksonXmlRootElement(
        localName = "GetTx",
        namespace = "http://www.dcep.com/dcep/42100101/"
)
public class Dcep42100101DTO extends GwDTO {
    /**
     * 组件MessageHeader
     */
    @JacksonXmlProperty(
            localName = "MsgHdr"
    )
    @NotNull
    @Valid
    private MsgHdr msgHdr;

    /**
     * 组件TransactionQueryDefinition
     */
    @JacksonXmlProperty(
            localName = "TxQryDef"
    )
    @NotNull
    @Valid
    private TxQryDef txQryDef;

    @Override
    public void init() {
    }

    @Override
    public String fetchMsgId() {
        return msgHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        return true;
    }
}
