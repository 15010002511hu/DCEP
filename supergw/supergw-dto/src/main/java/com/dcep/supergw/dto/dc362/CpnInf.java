package com.dcep.supergw.dto.dc362;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
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
 * @version : CpnInf.java v 0.1 2023-01-05
 * Copyright 2023 PBCDCI ALL Rights
 * @description :
 */
@Getter
@Setter
@ToString
public class CpnInf {
    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "PrmtGnrtdInstnId")
    String prmtGnrtdInstnId;

    @NotBlank
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "PrmtId")
    String prmtId;

    @NotBlank
    @Length(min = 1, max = 60)
    @JacksonXmlProperty(localName = "PrmtNm")
    String prmtNm;


    @Length(min = 1, max = 256)
    @JacksonXmlProperty(localName = "PrmtDescInf")
    String prmtDescInf;

    @NotBlank
    @Pattern(regexp = "PT0[1-9]||PT[1-9]\\d", message = "营销活动类型PrmtTp错误,范围为PT01-PT99")
    @JacksonXmlProperty(localName = "PrmtTp")
    String prmtTp;

    @NotBlank
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "CpnId")
    String cpnId;

    @Pattern(
            regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}",
            message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss"
    )
    @JacksonXmlProperty(localName = "CpnExp")
    String cpnExp;

    @NotNull
    @Valid
    @JacksonXmlProperty(localName = "CpnAmt")
    ActiveCurrencyAndAmount cpnAmt;

    @NotBlank
    @Pattern(regexp = "CRP01||CRP02||CRP03||CRP04")
    @JacksonXmlProperty(localName = "CpnRefPrprty")
    String cpnRefPrprty;

    @Length(min = 1, max = 256)
    @JacksonXmlProperty(localName = "CpnDescInf")
    String cpnDescInf;

    @Length(min = 1, max = 128)
    @JacksonXmlProperty(localName = "RmkInf")
    String rmkInf;
}
