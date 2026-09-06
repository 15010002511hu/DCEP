package com.dcep.supergw.dto.dc069;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SgntrWltInf implements Serializable {

    @NotBlank
    @Length(min = 1, max = 240)
    @JacksonXmlProperty(localName = "CstmrNm")
    String cstmrNm;

    @NotBlank
    @Length(min = 1, max = 68)
    @JacksonXmlProperty(localName = "WltId")
    String wltId;


    @Pattern(regexp = "WT[0-9]{2}")
    @JacksonXmlProperty(localName = "WltTp")
    String wltTp;


    @Pattern(regexp = "WL[0-9]{2}")
    @JacksonXmlProperty(localName = "WltLvl")
    String wltLvl;

    @Length(min = 1, max = 60)
    @JacksonXmlProperty(localName = "WltNm")
    String wltNm;

    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "InstnId")
    String instnId;

    @Length(min = 1, max = 512)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "CustData")
    String custData;
}
