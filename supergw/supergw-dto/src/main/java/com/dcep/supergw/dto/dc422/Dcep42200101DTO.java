package com.dcep.supergw.dto.dc422;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.supergw.validation.Check422Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * dcep.422.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:28:19
 */
@Data
@Gateway(
        msgTp = "dcep.422.001.01", isReturn = true
)
@JacksonXmlRootElement(
        localName = "RtrTx",
        namespace = "http://www.dcep.com/dcep/42200101/"
)
@Check422Biz(groups = Priority.Lowest.class)
public class Dcep42200101DTO extends GwDTO {
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
     * 组件ReportOrError
     */
    @JacksonXmlProperty(
            localName = "RptOrErr"
    )
    @NotNull
    @Valid
    private RptOrErr rptOrErr;

    /**
     * 组件SupplementaryData
     */
    @JacksonXmlProperty(
            localName = "SplmtryData"
    )
    @Valid
    private SplmtryData splmtryData;

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
