/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc202;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author liuqi
 * @version $Id: Rsn.java, v 0.1 2019年8月24日 下午2:54:27 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "Rsn")
@Getter
@Setter
@ToString
public class Rsn implements Serializable {
    /**  */
    private static final long serialVersionUID = -2729571199707220442L;
    /**  */
    /**
     * 业务拒绝码
     */
    @JacksonXmlProperty(localName = "Prtry")
    @Pattern(regexp = "^[R][0-9]{3}$")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            prtry;

}
