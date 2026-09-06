package com.dcep.supergw.dto.dc072;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RspnInf implements Serializable {

    @NotBlank
    @Pattern(regexp = "PR[0-9]{2}")
    @JacksonXmlProperty(localName = "RspnSts")
    private String rspnSts;

    @Pattern(regexp = "R[0-9]{3}")
    @JacksonXmlProperty(localName = "RjctCd")
    private String rjctCd;

    @Length(min = 1, max = 105)
    @JacksonXmlProperty(localName = "RjctInf")
    private String rjctInf;
}
