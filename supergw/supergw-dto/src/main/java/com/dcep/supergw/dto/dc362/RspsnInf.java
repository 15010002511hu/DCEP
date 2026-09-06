package com.dcep.supergw.dto.dc362;

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
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : RspsnInf.java v 0.1 2023-01-05
 * Copyright 2023 PBCDCI ALL Rights
 * @description :
 */
@Getter
@Setter
@ToString
public class RspsnInf implements Serializable {
    private static final long serialVersionUID = -5529978172520806105L;
    @NotBlank
    @Pattern(regexp = "PR00||PR01||PR02||PR03||PR04")
    @JacksonXmlProperty(localName = "RspsnSts")
    String rspsnSts;

    @Pattern(regexp = "R00[1-9]|||R0[1-9]\\d||R[1-9]\\d\\d", message = "RjctCd错误,范围为R001-R999")
    @JacksonXmlProperty(localName = "RjctCd")
    String rjctCd;

    @Length(min = 1, max = 105)
    @JacksonXmlProperty(localName = "RjctInf")
    String rjctInf;

    @NotNull
    @Valid
    @JacksonXmlProperty(localName = "TrxAmt")
    ActiveCurrencyAndAmount trxAmt;
}
