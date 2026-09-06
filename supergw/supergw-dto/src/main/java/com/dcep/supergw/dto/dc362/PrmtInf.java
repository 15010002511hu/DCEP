package com.dcep.supergw.dto.dc362;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : PrmtInf.java v 0.1 2023-01-05
 * Copyright 2023 PBCDCI ALL Rights
 * @description :
 */
@Getter
@Setter
@ToString
public class PrmtInf implements Serializable {

    private static final long serialVersionUID = 7284934333432897429L;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "CpnTtlAmt")
    ActiveCurrencyAndAmount cpnTtlAmt;


    @NotBlank
    @Length(min = 1, max = 15)
    @JacksonXmlProperty(localName = "CpnTtlNb")
    String cpnTtlNb;

    @Valid
    @NotNull
    @JacksonXmlElementWrapper(localName = "CpnInfList")
    @JacksonXmlProperty(localName = "CpnInf")
    List<CpnInf> cpnInfList;
}
