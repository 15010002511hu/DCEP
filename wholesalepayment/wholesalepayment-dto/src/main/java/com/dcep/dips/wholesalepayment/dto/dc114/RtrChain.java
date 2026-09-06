package com.dcep.dips.wholesalepayment.dto.dc114;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--ReturnChain
 * @Author zhaotianwu
 * @date 2025-10-13 17:13:58
 */
@Data
public class RtrChain implements Serializable {
    /**
     * 组件----Debtor
     */
    @JacksonXmlProperty(
            localName = "Dbtr"
    )
    @NotNull
    @Valid
    private Dbtr dbtr;

    /**
     * 组件----DebtorAccount
     */
    @JacksonXmlProperty(
            localName = "DbtrAcct"
    )
    @NotNull
    @Valid
    private DbtrAcct dbtrAcct;

    /**
     * 组件----DebtorAgent
     */
    @JacksonXmlProperty(
            localName = "DbtrAgt"
    )
    @NotNull
    @Valid
    private DbtrAgt dbtrAgt;

    /**
     * 组件----CreditorAgent
     */
    @JacksonXmlProperty(
            localName = "CdtrAgt"
    )
    @NotNull
    @Valid
    private CdtrAgt cdtrAgt;

    /**
     * 组件----Creditor
     */
    @JacksonXmlProperty(
            localName = "Cdtr"
    )
    @NotNull
    @Valid
    private Cdtr cdtr;

    /**
     * 组件----CreditorAccount
     */
    @JacksonXmlProperty(
            localName = "CdtrAcct"
    )
    @NotNull
    @Valid
    private CdtrAcct cdtrAcct;
}
