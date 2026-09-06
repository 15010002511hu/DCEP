package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--------------InstructedAgent
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:59:17
 */
@Data
public class InstdAgt implements Serializable {
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
