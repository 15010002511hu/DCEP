/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc201;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author liuqi
 * @version $Id: SvcLvl.java, v 0.1 2019年8月23日 下午7:33:14 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "SvcLvl")
@Getter
@Setter
@ToString
public class SvcLvl implements Serializable {
    /**  */
    private static final long serialVersionUID = 8204234670347388709L;
    /**
     * 支付方式
     */
    @JacksonXmlProperty(localName = "Prtry")
    @NotBlank
    @Pattern(regexp = "^[T]{2}[0-9]{2}")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            prtry;

}
