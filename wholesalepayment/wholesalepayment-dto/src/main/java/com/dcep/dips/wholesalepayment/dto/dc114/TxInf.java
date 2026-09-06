package com.dcep.dips.wholesalepayment.dto.dc114;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件TransactionInformation
 * @Author zhaotianwu
 * @date 2025-10-13 17:14:15
 */
@Data
public class TxInf implements Serializable {
    /**
     * 退汇标识号
     */
    @JacksonXmlProperty(
            localName = "RtrId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    private String rtrId;

    /**
     * 组件--OriginalGroupInformation
     */
    @JacksonXmlProperty(
            localName = "OrgnlGrpInf"
    )
    @NotNull
    @Valid
    private OrgnlGrpInf orgnlGrpInf;

    /**
     * 原指令标识号
     */
    @JacksonXmlProperty(
            localName = "OrgnlInstrId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String orgnlInstrId;

    /**
     * 原唯一标识号
     */
    @JacksonXmlProperty(
            localName = "OrgnlUETR"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String orgnlUETR;

    /**
     * 原交易金额
     */
    @JacksonXmlProperty(
            localName = "OrgnlIntrBkSttlmAmt"
    )
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount orgnlIntrBkSttlmAmt;

    /**
     * 原结算日期
     */
    @JacksonXmlProperty(
            localName = "OrgnlIntrBkSttlmDt"
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
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String orgnlIntrBkSttlmDt;

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
            localName = "RtrdIntrBkSttlmAmt"
    )
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount rtrdIntrBkSttlmAmt;

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
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
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
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    @Pattern(
            regexp = "URGT||HIGH||NORM"
    )
    private String sttlmPrty;

    /**
     * 发起参与机构
     */
    @JacksonXmlProperty(
            localName = "InstgAgt"
    )
    @NotNull
    @Valid
    private InstgAgt instgAgt;

    /**
     * 接收参与机构
     */
    @JacksonXmlProperty(
            localName = "InstdAgt"
    )
    @NotNull
    @Valid
    private InstdAgt instdAgt;

    /**
     * 组件--ReturnChain
     */
    @JacksonXmlProperty(
            localName = "RtrChain"
    )
    @NotNull
    @Valid
    private RtrChain rtrChain;

    /**
     * 组件--ReturnReasonInformation
     */
    @JacksonXmlProperty(
            localName = "RtrRsnInf"
    )
    @Valid
    private RtrRsnInf rtrRsnInf;

    /**
     * 组件--OriginalTransactionReference
     */
    @JacksonXmlProperty(
            localName = "OrgnlTxRef"
    )
    @NotNull
    @Valid
    private OrgnlTxRef orgnlTxRef;

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
