package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--------------InstructingAgent
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:59:20
 */
@Data
public class InstgAgt implements Serializable {
    /**
     * 组件----------------FinancialInstitutionIdentification
     */
    @JacksonXmlProperty(
            localName = "FinInstnId"
    )
    @NotNull
    @Valid
    private FinInstnId finInstnId;
}
