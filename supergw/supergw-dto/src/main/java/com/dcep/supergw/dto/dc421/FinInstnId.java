package com.dcep.supergw.dto.dc421;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 组件----------------FinancialInstitutionIdentification
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:26:37
 */
@Data
public class FinInstnId implements Serializable {
    /**
     * 组件------------------ClearingSystemMemberIdentification
     */
    @JacksonXmlProperty(
            localName = "ClrSysMmbId"
    )
    @NotNull
    @Valid
    private ClrSysMmbId clrSysMmbId;
}
