package com.dcep.dips.wholesalepayment.dto.dc427;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件------PaymentTypeInformation
 * @Author luteng
 * @date 2025-10-21 16:31:27
 */
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PmtTpInf implements Serializable {
    /**
     * 组件--------CategoryPurpose
     */
    @JacksonXmlProperty(
            localName = "CtgyPurp"
    )
    @NotNull
    @Valid
    private CtgyPurp ctgyPurp;
}
