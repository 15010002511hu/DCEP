package com.dcep.dips.wholesalepayment.dto.dc428;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件--TransactionInformationAndStatus
 * @Author luteng
 * @date 2025-10-21 17:29:45
 */
@Data
public class TxInfAndSts implements Serializable {
    /**
     * 原业务状态
     */
    @JacksonXmlProperty(
            localName = "CxlStsId"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String cxlStsId;

    /**
     * 组件----ResolvedCase
     */
    @JacksonXmlProperty(
            localName = "RslvdCase"
    )
    @NotNull
    @Valid
    private RslvdCase rslvdCase;

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
     * 组件----CancellationStatusReasonInformation
     */
    @JacksonXmlProperty(
            localName = "CxlStsRsnInf"
    )
    @NotNull
    @Valid
    private CxlStsRsnInf cxlStsRsnInf;
}
