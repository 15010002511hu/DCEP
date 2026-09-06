package com.dcep.dips.wholesalepayment.dto.dc427;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.UUID;

/**
 * 组件--TransactionInformation
 * @Author luteng
 * @date 2025-10-21 16:31:38
 */
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TxInf implements Serializable {
    /**
     * 组件----Case
     */
    @JacksonXmlProperty(
            localName = "Case"
    )
    @NotNull
    @Valid
    private Cas cas;  //这个地方生成的是case，跟java关键词冲突了，先改成cas

    /**
     * 组件----OriginalGroupInformation
     */
    @JacksonXmlProperty(
            localName = "OrgnlGrpInf"
    )
    @NotNull
    @Valid
    private OrgnlGrpInf orgnlGrpInf;

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
     * 组件----CancellationReasonInformation
     */
    @JacksonXmlProperty(
            localName = "CxlRsnInf"
    )
    @Valid
    private CxlRsnInf cxlRsnInf;

    /**
     * 组件----OriginalTransactionReference
     */
    @JacksonXmlProperty(
            localName = "OrgnlTxRef"
    )
    @NotNull
    @Valid
    private OrgnlTxRef orgnlTxRef;
}
