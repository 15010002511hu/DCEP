package com.dcep.dips.wholesalepayment.dto.dc200;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件TransactionInformationAndStatus
 * @Author luteng
 * @date 2025-09-28 11:40:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TxInfAndSts implements Serializable {
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
     * 原端到端标识
     */
    @JacksonXmlProperty(
            localName = "OrgnlEndToEndId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String orgnlEndToEndId;

    /**
     * 原交易标识号
     */
    @JacksonXmlProperty(
            localName = "OrgnlTxId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String orgnlTxId;

    /**
     * 原唯一标识号
     */
    @JacksonXmlProperty(
            localName = "OrgnlUETR"
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String orgnlUETR;

    /**
     * 业务状态
     */
    @JacksonXmlProperty(
            localName = "TxSts"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    @Pattern(
            regexp = "PR01||PR10||PR11||PR14"
    )
    private String txSts;

    /**
     * 组件--StatusReasonInformation
     */
    @JacksonXmlProperty(
            localName = "StsRsnInf"
    )
    @NotNull
    @Valid
    private StsRsnInf stsRsnInf;

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
    @Valid
    private SplmtryData splmtryData;
}
