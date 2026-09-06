package com.dcep.supergw.dto.dc323;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class MrchntInf implements Serializable {
    private static final long serialVersionUID = -2700637033442685231L;
    /**
     * 商户属性
     */
    @JacksonXmlProperty(localName = "MrchntPrprty")
    @Pattern(regexp = "MP[0-9]{2}")
    private String mrchntPrprty;
    /**
     * 商户编码
     */
    @JacksonXmlProperty(localName = "MrchntNo")
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @NotBlank
    private String mrchntNo;
    /**
     * 商户简称
     */
    @JacksonXmlProperty(localName = "MrchntAbbrNm")
    @Length(min = 1, max = 30)
    @NotBlank
    private String mrchntAbbrNm;
    /**
     * 商户名称
     */
    @JacksonXmlProperty(localName = "MrchntNm")
    @Length(min = 1, max = 60)
    @NotBlank
    private String mrchntNm;
    /**
     * 商户类别代码
     */
    @JacksonXmlProperty(localName = "MCC")
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mcc;
}
