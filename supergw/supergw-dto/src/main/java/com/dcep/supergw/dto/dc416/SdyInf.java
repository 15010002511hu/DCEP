package com.dcep.supergw.dto.dc416;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : SdyInf.java v 0.1 2020-04-28
 * @description :2021-11-16:修改SdyCd的长度为16
 */
@Getter
@Setter
@ToString
public class SdyInf {
    @JacksonXmlProperty(localName = "SdyNm")
    @NotBlank
    @Length(min = 1, max = 24)
    private String sdyNm;

    @JacksonXmlProperty(localName = "SdyCd")
    @NotBlank
    @Length(min = 1, max = 16)
    private String sdyCd;
}
