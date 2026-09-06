package com.dcep.dips.wholesalepayment.dto.dc181;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件LiquidityDebitTransfer
 * @Author luteng
 * @date 2025-09-05 16:32:09
 */
@Data
public class LqdtyDbtTrf implements Serializable {
    /**
     * 组件--发起参与机构
     */
    @JacksonXmlProperty(localName = "Cdtr")
    @NotNull
    @Valid
    private Cdtr cdtr;

    /**
     * 组件--交易金额
     */
    @JacksonXmlProperty(localName = "TrfdAmt")
    @NotNull
    @Valid
    private TrfdAmt trfdAmt;

    /**
     * 组件--调减参与机构
     */
    @JacksonXmlProperty(localName = "Dbtr")
    @NotNull
    @Valid
    private Dbtr dbtr;
}
