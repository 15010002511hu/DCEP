package com.dcep.supergw.dto.dc305;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
@Setter
@Getter
@ToString
public class SubMrchntInf implements Serializable {

    /**
     * 二级商户编码
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SubMrchntNo")
    private String subMrchntNo;

    /**
     * 二级商户钱包ID
     */
    @Length(min = 1, max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SubMrchntWltId")
    private String subMrchntWltId;

    /**
     * 二级商户名称
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 60)
    @JacksonXmlProperty(localName = "SubMrchntNm")
    private String subMrchntNm;

    /**
     * 二级商户简称
     */
    @Length(min = 1, max = 30)
    @JacksonXmlProperty(localName = "SubMrchntAbbrNm")
    private String subMrchntAbbrNm;


    /**
     * 二级商户类别代码
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SubMCC")
    private String subMCC;

    /**
     * 二级商户证件类型
     */
    @Length(min = 1, max = 4)
    @Pattern(regexp = "IT[0-9]{2}")
    @JacksonXmlProperty(localName = "SubMrchntIdTp")
    private String subMrchntIdTp;

    /**
     * 二级商户证件编码
     */
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SubMrchntIdNo")
    private String subMrchntIdNo;
}
