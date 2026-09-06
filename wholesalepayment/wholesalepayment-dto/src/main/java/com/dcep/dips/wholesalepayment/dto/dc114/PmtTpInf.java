package com.dcep.dips.wholesalepayment.dto.dc114;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--PaymentTypeInformation
 * @Author zhaotianwu
 * @date 2025-10-13 17:14:03
 */
@Data
public class PmtTpInf implements Serializable {
    /**
     * 组件----CategoryPurpose
     */
    @JacksonXmlProperty(
            localName = "CtgyPurp"
    )
    @NotNull
    @Valid
    private CtgyPurp ctgyPurp;
}
