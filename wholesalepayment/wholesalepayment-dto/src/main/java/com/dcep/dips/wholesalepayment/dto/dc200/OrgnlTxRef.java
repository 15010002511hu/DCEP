package com.dcep.dips.wholesalepayment.dto.dc200;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--OriginalTransactionReference
 * @Author luteng
 * @date 2025-09-28 11:40:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
