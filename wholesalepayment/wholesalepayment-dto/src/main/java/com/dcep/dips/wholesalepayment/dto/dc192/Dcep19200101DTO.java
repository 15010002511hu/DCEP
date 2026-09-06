package com.dcep.dips.wholesalepayment.dto.dc192;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * dcep.192.001.01DTO
 * @Author qiaopengyu
 * @date 2025-10-31 11:25:05
 */
@Data
@Gateway(
        msgTp = "dcep.192.001.01"
)
@JacksonXmlRootElement(
        localName = "FIToFIPmtStsRpt",
        namespace = "http://www.dcep.com/dcep/19200101/"
)
public class Dcep19200101DTO extends GwDTO {
    /**
     * 组件GroupHeader
     */
    @JacksonXmlProperty(
            localName = "GrpHd"
    )
    @NotNull
    @Valid
    private GrpHd grpHd;

    /**
     * 组件TransactionInformationAndStatus
     */
    @JacksonXmlProperty(
            localName = "TxInfAndSts"
    )
    @NotNull
    @Valid
    private TxInfAndSts txInfAndSts;

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
