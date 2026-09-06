package com.dcep.supergw.dto.dc320;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
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
    private static final long serialVersionUID = -3527765468155168431L;
    @NotBlank
    @Pattern(regexp = "PR00||PR01")
    @JacksonXmlProperty(localName = "RspsnSts")
    String rspsnSts;

    @Pattern(regexp = "R00[1-9]|||R0[1-9]\\d||R[1-9]\\d\\d", message = "RjctCd错误,范围为R001-R999")
    @JacksonXmlProperty(localName = "RjctCd")
    String rjctCd;

    @Length(min = 1, max = 105)
    @JacksonXmlProperty(localName = "RjctInf")
    String rjctInf;

    @Length(min = 1, max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "PmtCode")
    String pmtCode;

    @Length(min = 1, max = 15)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "PmtCodeExp")
    String pmtCodeExp;
}
