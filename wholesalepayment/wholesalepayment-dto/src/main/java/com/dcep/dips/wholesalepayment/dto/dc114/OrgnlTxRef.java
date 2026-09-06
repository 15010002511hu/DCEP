package com.dcep.dips.wholesalepayment.dto.dc114;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--OriginalTransactionReference
 * @Author zhaotianwu
 * @date 2025-10-13 17:13:38
 */
@Data
public class OrgnlTxRef implements Serializable {
    /**
     * 组件----CreditorAccount
     */
    @JacksonXmlProperty(
            localName = "CdtrAcct"
    )
    @NotNull
    @Valid
    private CdtrAcct cdtrAcct;

    /**
     * 组件----DebtorAccount
     */
    @JacksonXmlProperty(
            localName = "DbtrAcct"
    )
    @NotNull
    @Valid
    private DbtrAcct dbtrAcct;
}
