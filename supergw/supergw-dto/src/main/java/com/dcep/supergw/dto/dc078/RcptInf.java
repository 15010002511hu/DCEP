package com.dcep.supergw.dto.dc078;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RcptInf implements Serializable {

    @NotBlank
    @Pattern(regexp = "PR00||PR01||PR02||PR03||PR04")
    @JacksonXmlProperty(localName = "PrcSts")
    String prcSts;

    @Pattern(regexp = "R[0-9]{3}")
    @JacksonXmlProperty(localName = "PrcCd")
    String prcCd;


    @Length(min = 1, max = 105)
    @JacksonXmlProperty(localName = "RjctInf")
    String rjctInf;
}
