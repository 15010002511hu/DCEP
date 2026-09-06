package com.dcep.dips.wholesalepayment.dto.dc427;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件----OriginalTransactionReference
 * @Author luteng
 * @date 2025-10-21 16:31:31
 */
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrgnlTxRef implements Serializable {
    /**
     * 原交易金额
     */
    @JacksonXmlProperty(
            localName = "IntrBkSttlmAmt"
    )
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount intrBkSttlmAmt;

    /**
     * 原付款参与机构
     */
    @JacksonXmlProperty(
            localName = "DbtrAgt"
    )
    @NotNull
    @Valid
    private DbtrAgt dbtrAgt;

    /**
     * 原收款参与机构
     */
    @JacksonXmlProperty(
            localName = "CdtrAgt"
    )
    @NotNull
    @Valid
    private CdtrAgt cdtrAgt;

    /**
     * 组件------PaymentTypeInformation
     */
    @JacksonXmlProperty(
            localName = "PmtTpInf"
    )
    @NotNull
    @Valid
    private PmtTpInf pmtTpInf;

    /**
     * 组件------Purpose
     */
    @JacksonXmlProperty(
            localName = "Purp"
    )
    @NotNull
    @Valid
    private Purp purp;
}
