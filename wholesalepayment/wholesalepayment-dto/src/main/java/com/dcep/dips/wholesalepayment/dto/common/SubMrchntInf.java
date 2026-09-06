package com.dcep.dips.wholesalepayment.dto.common;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 二级商户信息
 *
 * @author caoxiaogai
 *
 */
@JacksonXmlRootElement(localName = "SubMrchntInf")
@Setter
@Getter
@ToString
public class SubMrchntInf implements Serializable {

    private static final long serialVersionUID = -2915320423401117222L;
    /**
     * 二级商户编码
     */
    @JacksonXmlProperty(localName = "SubMrchntNo")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            subMrchntNo;

    /**
     * 二级商户钱包ID
     */
    @JacksonXmlProperty(localName = "SubMrchntWltId")
    @Length(min = 16, max = 16)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            subMrchntWltId;

    /**
     * 二级商户名称
     */
    @JacksonXmlProperty(localName = "SubMrchntNm")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 60)
    private String subMrchntNm;

    /**
     * 二级商户简称
     */
    @JacksonXmlProperty(localName = "SubMrchntAbbrNm")
    @Length(min = 1, max = 30)
    private String subMrchntAbbrNm;

    /**
     * 二级商户类别代码
     */
    @JacksonXmlProperty(localName = "SubMCC")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String subMCC;

    /**
     * 二级商户证件类型
     */
    @JacksonXmlProperty(localName = "SubMrchntIdTp")
    @Pattern(regexp = "^[I][T][0-9]{2}")
    private String subMrchntIdTp;

    /**
     * 二级商户证件编码
     */
    @JacksonXmlProperty(localName = "SubMrchntIdNo")
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String subMrchntIdNo;
}
