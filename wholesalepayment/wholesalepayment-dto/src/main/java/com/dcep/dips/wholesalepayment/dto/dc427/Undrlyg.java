package com.dcep.dips.wholesalepayment.dto.dc427;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件Underlying
 * @Author luteng
 * @date 2025-10-21 16:31:39
 */
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Undrlyg implements Serializable {
    /**
     * 组件--TransactionInformation
     */
    @JacksonXmlProperty(
            localName = "TxInf"
    )
    @NotNull
    @Valid
    private TxInf txInf;
}
