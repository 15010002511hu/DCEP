package com.dcep.dips.wholesalepayment.dto.dc183;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件----FinancialInstitutionIdentification
 * @Author luteng
 * @date 2025-09-05 16:39:40
 */
@Data
public class FinInstnId implements Serializable {
    /**
     * 组件------ClearingSystemMemberIdentification
     */
    @JacksonXmlProperty(
            localName = "ClrSysMmbId"
    )
    @NotNull
    @Valid
    private ClrSysMmbId clrSysMmbId;
}
