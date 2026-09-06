package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--BusinessReport
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:28:13
 */
@Data
public class BizRpt implements Serializable {
    /**
     * 组件----PaymentCommonInformation
     */
    @JacksonXmlProperty(
            localName = "PmtCmonInf"
    )
    @Valid
    private PmtCmonInf pmtCmonInf;

    /**
     * 组件----TransactionReport
     */
    @JacksonXmlProperty(
            localName = "TxRpt"
    )
    @NotNull
    @Valid
    private TxRpt txRpt;
}
