package com.dcep.supergw.dto.dc018;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PmtMsg implements Serializable {

    @NotBlank
    @Pattern(regexp = "SCOCT[0-9]{2}")
    @JacksonXmlProperty(localName = "CmdTp")
    String cmdTp;

    @NotBlank
    @Length(min = 1,max = 128)
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    @JacksonXmlProperty(localName = "CallPth")
    String callPth;

    @NotBlank
    @Length(min = 1,max = 30)
    @JacksonXmlProperty(localName = "Pmt")
    String pmt;
}
