package com.dcep.supergw.dto.dc421;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * 组件------------LongBusinessIdentification
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:26:42
 */
@Data
public class LngBizId implements Serializable {

    /**
     * 唯一标识号
     */
    @JacksonXmlProperty(
        localName = "UETR"
    )
    @NotBlank
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    private String uETR;

    /**
     * 组件--------------InterBankSettlementAmount
     */
    @JacksonXmlProperty(
        localName = "IntrBkSttlmAmt"
    )
    @NotBlank
    private String intrBkSttlmAmt;

    /**
     * 原报文交易日期
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
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    private String intrBkSttlmDt;

    /**
     * 组件--------------PaymentMethod
     */
    @JacksonXmlProperty(
        localName = "PmtMtd"
    )
    @NotNull
    @Valid
    private PmtMtd pmtMtd;

    /**
     * 组件--------------InstructingAgent
     */
    @JacksonXmlProperty(
        localName = "InstgAgt"
    )
    @NotNull
    @Valid
    private InstgAgt instgAgt;

    /**
     * 组件--------------InstructedAgent
     */
    @JacksonXmlProperty(
        localName = "InstdAgt"
    )
    @NotNull
    @Valid
    private InstdAgt instdAgt;
}
