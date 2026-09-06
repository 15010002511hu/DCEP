package com.dcep.dips.wholesalepayment.dto.dc192;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--OriginalTransactionReference
 * @Author qiaopengyu
 * @date 2025-10-31 11:24:55
 */
@Data
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
