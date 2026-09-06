package com.dcep.supergw.dto.dc309;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class CpnInf implements Serializable {

    @JacksonXmlProperty(localName = "PrmtId")
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    String prmtId;

    @JacksonXmlProperty(localName = "CpnId")
    @Length(min = 1, max = 64)
    String cpnId;

    @JacksonXmlProperty(localName = "CpnAmt")
    @Valid
    ActiveCurrencyAndAmount cpnAmt;
}
