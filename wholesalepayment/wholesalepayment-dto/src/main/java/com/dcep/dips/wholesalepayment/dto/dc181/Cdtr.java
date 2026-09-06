package com.dcep.dips.wholesalepayment.dto.dc181;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--Creditor
 * @Author luteng
 * @date 2025-09-05 16:32:06
 */
@Data
public class Cdtr implements Serializable {
    /**
     * 组件----FinancialInstitutionIdentification
     */
    @JacksonXmlProperty(localName = "FinInstnId")
    @NotNull
    @Valid
    private FinInstnId finInstnId;
}
