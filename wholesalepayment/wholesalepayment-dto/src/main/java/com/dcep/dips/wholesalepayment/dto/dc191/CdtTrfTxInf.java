package com.dcep.dips.wholesalepayment.dto.dc191;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 交易信息
 * @Author qiaopengyu
 * @date 2025-10-30 21:17:19
 */
@Data
public class CdtTrfTxInf implements Serializable {
    private static final long serialVersionUID = 2051484380512826002L;
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
     * 金额
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
    @NotBlank
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
    private String sttlmPrty;

    /**
     * 组件--SettlementTimeIndication
     */
    @JacksonXmlProperty(
            localName = "SttlmTmIndctn"
    )
    @Valid
    private SttlmTmIndctn sttlmTmIndctn;

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
     * 组件--PreviousInstructingAgent1
     */
    @JacksonXmlProperty(
            localName = "PrvsInstgAgt1"
    )
    @Valid
    private PrvsInstgAgt1 prvsInstgAgt1;

    /**
     * 组件--IntermediaryAgent1
     */
    @JacksonXmlProperty(
            localName = "IntrmyAgt1"
    )
    @Valid
    private IntrmyAgt1 intrmyAgt1;

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
     * 付款方
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
    @Valid
    private DbtrAcct dbtrAcct;

    /**
     * 付款方代理
     */
    @JacksonXmlProperty(
            localName = "DbtrAgt"
    )
    @Valid
    private DbtrAgt dbtrAgt;

    /**
     * 付款方代理账户
     */
    @JacksonXmlProperty(
            localName = "DbtrAgtAcct"
    )
    @Valid
    private DbtrAgtAcct dbtrAgtAcct;

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
    @Valid
    private CdtrAcct cdtrAcct;

    /**
     * 收款方代理
     */
    @JacksonXmlProperty(
            localName = "CdtrAgt"
    )
    @Valid
    private CdtrAgt cdtrAgt;

    /**
     * 收款方代理账户
     */
    @JacksonXmlProperty(
            localName = "CdtrAgtAcct"
    )
    @Valid
    private CdtrAgtAcct cdtrAgtAcct;

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
     * 附言 RmtInf
     * RemittanceInformation
     */
    @JacksonXmlElementWrapper(localName = "RmtInf")
    @JacksonXmlProperty(localName = "Ustrd")
    @Valid
    private List<String> ustrds;

    /**
     * RemittanceInformation
     * 将ustrds转换为RmtInf对象
     */
    @Valid
    @JsonIgnore
    private RmtInf rmtInf;

}
