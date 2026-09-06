/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc201;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
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
 * @version $Id: SttlmInf.java, v 0.1 2019年8月23日 下午6:03:59 liuqi Exp $
 */

@JacksonXmlRootElement(localName = "SttlmInf")
@Getter
@Setter
@ToString
public class SttlmInf implements Serializable{
    /**  */
    private static final long serialVersionUID = -4090842412289183121L;
    
    /**
     *   结算方式
     *
     */
    @JacksonXmlProperty(localName = "SttlmMtd")
    @NotBlank
    @Length(min = 4, max = 4)
    @Pattern(regexp = "CLRG")
    private String            sttlmMtd;

    /**
     * 
     */
    public SttlmInf() {
        super();
    }

    /**
     * @param sttlmMtd
     */
    public SttlmInf(String sttlmMtd) {
        this.sttlmMtd = sttlmMtd;
    }
    
    

}
