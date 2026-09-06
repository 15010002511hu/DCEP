package com.dcep.supergw.dto.dc028;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SgntrInstnInf implements Serializable {
    @NotBlank
    @Pattern(regexp = "IST[0-9]{2}")
    @JacksonXmlProperty(localName = "SgntrInstnTp")
    String sgntrInstnTp;

    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SgntrPmryInstnId")
    String sgntrPmryInstnId;

    @NotBlank
    @Length(min = 1, max = 60)
    @JacksonXmlProperty(localName = "SgntrPmryInstnNm")
    String sgntrPmryInstnNm;

    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SgntrScndryInstnId")
    String sgntrScndryInstnId;

    @Length(min = 1, max = 60)
    @JacksonXmlProperty(localName = "SgntrScndryInsnNm")
    String sgntrScndryInsnNm;

}
