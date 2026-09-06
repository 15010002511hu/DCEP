package com.dcep.dips.wholesalepayment.dto.dc112;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件CreditTransferTransactionInformation
 * @Author zhaotianwu
 * @date 2025-10-13 16:56:26
 */
@Data
public class CdtTrfTxInf implements Serializable {
    /**
     * 组件--PaymentIdentification
     */
    @JacksonXmlProperty(
            localName = "PmtId"
    )
    @NotNull
    @Valid
    private PmtId pmtId;

    /**
     * 组件--PaymentTypeInformation
     */
    @JacksonXmlProperty(
            localName = "PmtTpInf"
    )
    @NotNull
    @Valid
    private PmtTpInf pmtTpInf;

    /**
     * 交易金额
     */
    @JacksonXmlProperty(
            localName = "IntrBkSttlmAmt"
    )
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount intrBkSttlmAmt;

    /**
     * 期望结算日期
     */
    @JacksonXmlProperty(
            localName = "IntrBkSttlmDt"
    )
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd"
    )
    @Pattern(
            regexp = "(\\d{4}-\\d{2}-\\d{2})?",
            message = "日期格式错误，正确格式是：yyyy-MM-dd"
    )
    private String intrBkSttlmDt;

    /**
     * 业务优先级
     */
    @JacksonXmlProperty(
            localName = "SttlmPrty"
    )
    @NotBlank
    @Pattern(
            regexp = "URGT||HIGH||NORM"
    )
    private String sttlmPrty;

    /**
     * 组件--InstructingAgent
     */
    @JacksonXmlProperty(
            localName = "InstgAgt"
    )
    @NotNull
    @Valid
    private InstgAgt instgAgt;

    /**
     * 组件--InstructedAgent
     */
    @JacksonXmlProperty(
            localName = "InstdAgt"
    )
    @NotNull
    @Valid
    private InstdAgt instdAgt;

    /**
     * 组件--IntermediaryAgent1
     */
    @JacksonXmlProperty(
            localName = "IntrmyAgt1"
    )
    @Valid
    private IntrmyAgt1 intrmyAgt1;

    /**
     * 组件--IntermediaryAgent2
     */
    @JacksonXmlProperty(
            localName = "IntrmyAgt2"
    )
    @Valid
    private IntrmyAgt2 intrmyAgt2;

    /**
     * 组件--Debtor
     */
    @JacksonXmlProperty(
            localName = "Dbtr"
    )
    @NotNull
    @Valid
    private Dbtr dbtr;

    /**
     * 组件--DebtorAccount
     */
    @JacksonXmlProperty(
            localName = "DbtrAcct"
    )
    @NotNull
    @Valid
    private DbtrAcct dbtrAcct;

    /**
     * 组件--DebtorAgent
     */
    @JacksonXmlProperty(
            localName = "DbtrAgt"
    )
    @NotNull
    @Valid
    private DbtrAgt dbtrAgt;

    /**
     * 组件--CreditorAgent
     */
    @JacksonXmlProperty(
            localName = "CdtrAgt"
    )
    @NotNull
    @Valid
    private CdtrAgt cdtrAgt;

    /**
     * 收款方
     */
    @JacksonXmlProperty(
            localName = "Cdtr"
    )
    @NotNull
    @Valid
    private Cdtr cdtr;

    /**
     * 组件--CreditorAccount
     */
    @JacksonXmlProperty(
            localName = "CdtrAcct"
    )
    @NotNull
    @Valid
    private CdtrAcct cdtrAcct;

    /**
     * 组件--Purpose
     */
    @JacksonXmlProperty(
            localName = "Purp"
    )
    @NotNull
    @Valid
    private Purp purp;

    /**
     * 组件--RemittanceInformation
     */
    @JacksonXmlProperty(
            localName = "RmtInf"
    )
    @NotNull
    @Valid
    private RmtInf rmtInf;

    /**
     * 组件--SupplementaryData
     */
    @JacksonXmlProperty(
            localName = "SplmtryData"
    )
    @NotNull
    @Valid
    private SplmtryData splmtryData;
}
