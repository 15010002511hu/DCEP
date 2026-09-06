package com.dcep.supergw.dto.dc305;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @Author weiqianjing
 * @Date 2021/9/26 15:38
 * @Description:新架构版本中：商户证件类型、商户证件编码、场景 ID”必填
 */
@Setter
@Getter
@ToString
public class CdtrInf implements Serializable {

    private static final long serialVersionUID = -1819301227434314645L;
    /**
     * 商户所属运营机构
     */
    @JacksonXmlProperty(localName = "CdtrInstnId")
    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String cdtrInstnId;
    /**
     * 商户钱包ID
     */
    @JacksonXmlProperty(localName = "MrchntWltId")
    @Length(min = 1, max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mrchntWltId;
    /**
     * 商户属性
     */
    @Pattern(regexp = "MP[0-9]{2}")
    @JacksonXmlProperty(localName = "MrchntPrprty")
    private String mrchntPrprty;
    /**
     * 商户编码
     */
    @JacksonXmlProperty(localName = "MrchntNo")
    @Length(min = 1, max = 35)
    @NotBlank
    private String mrchntNo;
    /**
     * 商户名称
     */
    @JacksonXmlProperty(localName = "MrchntNm")
    @NotBlank
    @Length(min = 1, max = 60)
    private String mrchntNm;
    /**
     * 商户简称
     */
    @JacksonXmlProperty(localName = "MrchntAbbrNm")
    @Length(min = 1, max = 30)
    @NotBlank
    private String mrchntAbbrNm;
    /**
     * 商户类别代码
     */
    @JacksonXmlProperty(localName = "MCC")
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mcc;
    /**
     * 子钱包商户号
     */
    @JacksonXmlProperty(localName = "SubWltMrchntNo")
    @Length(min = 1, max = 35)
    private String subWltMrchntNo;

    /**
     * 商户证件类型
     */
    @JacksonXmlProperty(localName = "MrchntIdTp")
    @Length(min = 4, max = 4)
    @Pattern(regexp = "IT[0-9]{2}")
    private String mrchntIdTp;

    /**
     * 商户证件编码
     */
    @JacksonXmlProperty(localName = "MrchntIdNo")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 32)
    private String mrchntIdNo;

    /**
     * 场景ID
     */
    @JacksonXmlProperty(localName = "SceneId")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 10)
    private String sceneId;
}
