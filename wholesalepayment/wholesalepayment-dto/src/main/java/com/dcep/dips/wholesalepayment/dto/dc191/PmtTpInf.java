package com.dcep.dips.wholesalepayment.dto.dc191;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--PaymentTypeInformation
 * @Author qiaopengyu
 * @date 2025-10-30 21:17:03
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
