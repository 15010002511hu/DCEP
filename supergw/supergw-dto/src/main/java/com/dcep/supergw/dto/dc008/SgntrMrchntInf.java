package com.dcep.supergw.dto.dc008;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SgntrMrchntInf implements Serializable {

    @NotBlank
    @Length(min = 1,max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "MrchntId")
    String mrchntId;

    @NotBlank
    @Length(min = 1,max = 60)
    @JacksonXmlProperty(localName = "MrchntNm")
    String mrchntNm;

    @NotBlank
    @Length(min = 1,max = 30)
    @JacksonXmlProperty(localName = "MrchntAbbrvtdNm")
    String mrchntAbbrvtdNm;

    @Length(min = 1,max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "AcqrgAgtInstnId")
    String acqrgAgtInstnId;

    @Length(min = 1,max = 60)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "AcqrgAgtNm")
    String acqrgAgtNm;

    @NotBlank
    @Length(min = 1,max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "InstnId")
    String instnId;

    @NotBlank
    @Length(min = 1,max = 60)
    @JacksonXmlProperty(localName = "InstnNm")
    String instnNm;

}
