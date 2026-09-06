package com.dcep.dips.wholesalepayment.dto.dc112;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--PaymentTypeInformation
 * @Author zhaotianwu
 * @date 2025-10-13 16:56:03
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
