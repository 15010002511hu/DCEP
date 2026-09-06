package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 组件------------MemberIdentification
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:28:00
 */
@Data
public class MmbId implements Serializable {
    /**
     * 组件--------------FinancialInstitutionIdentification
     */
    @JacksonXmlProperty(
            localName = "FinInstnId"
    )
    @NotNull
    @Valid
    private FinInstnId finInstnId;
}
