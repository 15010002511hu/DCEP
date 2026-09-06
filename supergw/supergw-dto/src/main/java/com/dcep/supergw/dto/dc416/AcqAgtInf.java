package com.dcep.supergw.dto.dc416;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class AcqAgtInf implements Serializable {

    @NotBlank
    @JacksonXmlProperty(localName = "AcqAgtInstnId")
    @Length(min = 1,max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  acqAgtInstnId;

    @NotBlank
    @JacksonXmlProperty(localName = "AcqAgtNm")
    @Length(min = 1,max = 60)
    private String                  acqAgtNm;
}
