/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc202;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author liuqi
 * @version $Id: PmtTpInf.java, v 0.1 2019年8月24日 下午3:24:09 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "PmtTpInf")
@Getter
@Setter
@ToString
public class PmtTpInf implements Serializable{
    
    
    /**  */
    private static final long serialVersionUID = 4861853860151817709L;
    /**
     * CategoryPurpose
     */
    @JacksonXmlProperty(localName = "CtgyPurp")
    @Valid
    @NotNull
    private CtgyPurp          ctgyPurp;
    

}
