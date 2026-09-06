package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件----BankTransactionCode
 * @Author luteng
 * @date 2025-10-12 16:16:56
 */
@Data
public class BkTxCd implements Serializable {
    /**
     * 组件------Proprietary
     */
    @JacksonXmlProperty(
            localName = "Prtry"
    )
    @NotNull
    @Valid
    private Prtry prtry;
}
