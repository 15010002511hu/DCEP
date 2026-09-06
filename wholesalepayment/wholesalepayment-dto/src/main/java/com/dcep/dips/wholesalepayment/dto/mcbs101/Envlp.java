package com.dcep.dips.wholesalepayment.dto.mcbs101;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class Envlp {

    @JacksonXmlProperty(
            localName = "Cnts"
    )
    @NotNull
    @Valid
    private Cnts cnts;
}
