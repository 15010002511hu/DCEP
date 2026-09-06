package com.dcep.dips.wholesalepayment.dto.dc183;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件LiquidityDebitTransfer
 * @Author luteng
 * @date 2025-09-05 16:39:44
 */
@Data
public class LqdtyDbtTrf implements Serializable {
    /**
     * 组件--Creditor
     */
    @JacksonXmlProperty(
            localName = "Cdtr"
    )
    @NotNull
    @Valid
    private Cdtr cdtr;

    /**
     * 组件--TransferredAmount
     */
    @JacksonXmlProperty(
            localName = "TrfdAmt"
    )
    @NotNull
    @Valid
    private TrfdAmt trfdAmt;

    /**
     * 组件--Debtor
     */
    @JacksonXmlProperty(
            localName = "Dbtr"
    )
    @NotNull
    @Valid
    private Dbtr dbtr;
}
