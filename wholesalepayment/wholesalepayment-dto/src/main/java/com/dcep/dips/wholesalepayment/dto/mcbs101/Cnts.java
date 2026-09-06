package com.dcep.dips.wholesalepayment.dto.mcbs101;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class Cnts {
    @Length(
            min = 1,
            max = 13
    )
    @JacksonXmlProperty(
            localName = "BatchNO"
    )
    @NotBlank
    private String batchNO;

    @Length(
            min = 1,
            max = 32
    )
    @JacksonXmlProperty(
            localName = "ParamId"
    )
    @NotBlank
    private String paramId;
}
