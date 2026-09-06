package com.dcep.supergw.dto.dc361;

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
 * @version : MrchntInf.java v 0.1 2023-01-05
 * Copyright 2023 PBCDCI ALL Rights
 * @description :
 */
@Getter
@Setter
@ToString
public class MrchntInf implements Serializable {
    private static final long serialVersionUID = -5291435369500873951L;
    @NotBlank
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "MrchntNo")
    String mrchntNo;

    @NotBlank
    @Length(min = 1, max = 30)
    @JacksonXmlProperty(localName = "MrchntAbbrNm")
    String mrchntAbbrNm;

    @NotBlank
    @Length(min = 1, max = 60)
    @JacksonXmlProperty(localName = "MrchntNm")
    String mrchntNm;
}
