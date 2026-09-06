package com.dcep.supergw.dto.dc319;

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
 * @version : PtcInf.java v 0.1 2023-01-04
 * Copyright 2023 PBCDCI ALL Rights
 * @description :
 */
@Getter
@Setter
@ToString
public class PtcInf implements Serializable {
    private static final long serialVersionUID = -5189118555457836630L;

    @NotBlank
    @Length(min = 1, max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "PtcId")
    String ptcId;
    @NotBlank
    @Pattern(regexp = "IT0[1-9]||IT[1-9]\\d", message = "MrchntIdTp错误,范围为ITO1-IT99")
    @JacksonXmlProperty(localName = "MrchntIdTp")
    String mrchntIdTp;

    @NotBlank
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "MrchntIdNo")
    String mrchntIdNo;

    @NotBlank
    @Length(min = 1, max = 10)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SceneId")
    String sceneId;

}
