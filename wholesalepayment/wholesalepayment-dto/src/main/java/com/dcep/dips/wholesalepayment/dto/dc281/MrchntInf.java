/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc281;

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
 * 
 * @author gcg
 * @version $Id: MrchntInf.java, v 0.1 2019年8月27日下午13:16:59 gcg Exp $
 */
@JacksonXmlRootElement(localName = "MrchntInf")
@Setter
@Getter
@ToString
public class MrchntInf implements Serializable {
    /**  */
    private static final long serialVersionUID = -7732034558775980582L;

    /*
     * 商户号
     */
    @JacksonXmlProperty(localName = "MrchntNo")
    @NotBlank
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            mrchntNo;

    /*
     * 商户简称
     */
    @JacksonXmlProperty(localName = "MrchntAbbrNm")
    @NotBlank
    @Length(min = 1, max = 30)
    private String            mrchntAbbrNm;

    /**
     * 商户名称
     */
    @JacksonXmlProperty(localName = "MrchntNm")
    @NotBlank
    @Length(min = 1, max = 60)
    private String            mrchntNm;
    
    /**
     * 商户类别代码
     */
    @JacksonXmlProperty(localName = "MCC")
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mcc;

    /**
     * 商户证件类型
     */
    @JacksonXmlProperty(localName = "MrchntIdTp")
    @Pattern(regexp = "^[I][T][0-9]{2}")
    private String            mrchntIdTp;

    /**
     * 商户证件编码
     */
    @JacksonXmlProperty(localName = "MrchntIdNo")
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            mrchntIdNo;

    /**
     * 商品名称
     */
    @JacksonXmlProperty(localName = "GdNm")
    @NotBlank
    @Length(min = 1, max = 200)
    private String            gdNm;

    /**
     * 商户退款单号
     */
    @JacksonXmlProperty(localName = "OutRefNo")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            outRefNo;

}
