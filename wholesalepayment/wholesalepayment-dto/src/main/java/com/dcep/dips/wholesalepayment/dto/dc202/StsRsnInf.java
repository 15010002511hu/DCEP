/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc202;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author liuqi
 * @version $Id: StsRsnInf.java, v 0.1 2019年8月24日 下午2:33:47 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "StsRsnInf")
@Getter
@Setter
@ToString
public class StsRsnInf implements Serializable {
    /**  */
    private static final long serialVersionUID = -5866618476825245922L;
    /**
     * 业务状态
     */
    @JacksonXmlProperty(localName = "AddtlInf")
    @Length(min = 4, max = 4)
    @Pattern(regexp = "^[P][R][0-9]{2}")
    private String            addtlInf;

}
