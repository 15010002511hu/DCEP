package com.dcep.dips.wholesalepayment.dto.dc212;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JacksonXmlRootElement(localName = "SplmtryData")
@Getter
@Setter
@ToString
public class SplmtryData  implements Serializable {

    private static final long serialVersionUID = -8200604792176526080L;

    @JacksonXmlProperty(localName = "PlcAndNm")
    @NotBlank
    @Pattern(regexp = "/Document/FIToFIPmtStsRpt/TxInfAndSts")
    private String plcAndNm;

    @JacksonXmlProperty(localName = "Envlp")
    @Valid
    @NotNull
    private Envlp envlp;

}
