package com.dcep.supergw.dto.dc074;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RspnInf implements Serializable {

    @NotBlank
    @Pattern(regexp = "PR[0-9]{2}")
    @JacksonXmlProperty(localName = "RspnSts")
    private String rspnSts;

    @Pattern(regexp = "R[0-9]{3}")
    @JacksonXmlProperty(localName = "RjctCd")
    private String rjctCd;

    @JacksonXmlProperty(localName = "RjctInf")
    @Valid
    private RjctInf rjctInf;

}
