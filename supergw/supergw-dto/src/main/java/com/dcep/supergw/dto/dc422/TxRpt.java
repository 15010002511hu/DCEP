package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件----TransactionReport
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:28:07
 */
@Data
public class TxRpt implements Serializable {
    /**
     * 组件------PaymentIdentification
     */
    @JacksonXmlProperty(
            localName = "PmtId"
    )
    @NotNull
    @Valid
    private PmtId pmtId;

    /**
     * 组件------TransactionOrError
     */
    @JacksonXmlProperty(
            localName = "TxOrErr"
    )
    @NotNull
    @Valid
    private TxOrErr txOrErr;
}
