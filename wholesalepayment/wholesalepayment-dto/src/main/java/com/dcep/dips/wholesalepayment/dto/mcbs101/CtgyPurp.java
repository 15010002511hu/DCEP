package com.dcep.dips.wholesalepayment.dto.mcbs101;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class CtgyPurp {
    /**
     * 业务类型
     */
    @JacksonXmlProperty(
            localName = "cd"
    )
    @Length(
            min = 1,
            max = 4
    )
    @NotNull
    @Valid
    private String cd;
}
