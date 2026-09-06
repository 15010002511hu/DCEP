package com.dcep.supergw.dto.dc069;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RmngFndList implements Serializable {

    @NotBlank
    @Length(min = 1, max = 240)
    @JacksonXmlProperty(localName = "CustmrNm")
    String custmrNm;

    @NotBlank
    @Length(min = 1, max = 68)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "WltId")
    String wltId;

    @NotBlank
    @Pattern(regexp = "WT[0-9]{2}")
    @JacksonXmlProperty(localName = "WltTp")
    String wltTp;

    @NotBlank
    @Pattern(regexp = "WL[0-9]{2}")
    @JacksonXmlProperty(localName = "WltLvl")
    String wltLvl;

    @NotBlank
    @Valid
    @JacksonXmlProperty(localName = "TxAmt")
    ActiveCurrencyAndAmount txAmt;

    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "WltPty")
    String wltPty;
}
