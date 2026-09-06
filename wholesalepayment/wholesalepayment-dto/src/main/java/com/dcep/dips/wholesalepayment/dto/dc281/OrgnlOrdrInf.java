/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc281;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 *
 * @version $Id: OrgnlOrdrInf.java, v 0.1 2019年8月27日下午13:16:29 gcg Exp $
 */
@JacksonXmlRootElement(localName = "OrgnlOrdrInf")
@Setter
@Getter
@ToString
public class OrgnlOrdrInf implements Serializable {
    /**  */
    private static final long serialVersionUID = -7732034558775980582L;

    /*
     * 原订单号
     */
    @JacksonXmlProperty(localName = "OrgnlOrdrNo")
    @NotBlank
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            orgnlOrdrNo;

    /**
     * 原订单时间
     */
    @JacksonXmlProperty(localName = "OrgnlOrdrTm")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
    private String            orgnlOrdrTm;
}
