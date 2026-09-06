package com.dcep.dips.wholesalepayment.dto.dc428;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件CancellationDetails
 * @Author luteng
 * @date 2025-10-21 17:29:46
 */
@Data
public class CxlDtls implements Serializable {
    /**
     * 组件--TransactionInformationAndStatus
     */
    @JacksonXmlProperty(
            localName = "TxInfAndSts"
    )
    @NotNull
    @Valid
    private TxInfAndSts txInfAndSts;
}
