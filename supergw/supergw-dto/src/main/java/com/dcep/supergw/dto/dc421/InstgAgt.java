package com.dcep.supergw.dto.dc421;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 组件--------------InstructingAgent
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:26:38
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
