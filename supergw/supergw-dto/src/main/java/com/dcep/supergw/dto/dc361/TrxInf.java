package com.dcep.supergw.dto.dc361;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author : maxinyu
 * @version : TrxInf.java v 0.1 2023-01-05
 * Copyright 2023 PBCDCI ALL Rights
 * @description :
 */
@Getter
@Setter
@ToString
public class TrxInf implements Serializable {
    private static final long serialVersionUID = 4112811502788086819L;

    @NotBlank
    @Pattern(regexp = "TT01||TT02||TT14||TT15")
    @JacksonXmlProperty(localName = "TrxTp")
    String trxTp;

    @NotNull
    @Valid
    @JacksonXmlProperty(localName = "TrxAmt")
    ActiveCurrencyAndAmount trxAmt;
}
