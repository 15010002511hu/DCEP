/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc212;

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
 * @version $Id: OrgnlCdtrAgt.java, v 0.1 2019年8月24日 下午3:32:40 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "OrgnlCdtrAgt")
@Getter
@Setter
@ToString
public class OrgnlCdtrAgt implements Serializable{/**  */
    private static final long serialVersionUID = 2987692098904502375L;
    /**
     * FinancialInstitutionIdentification
     */
    @JacksonXmlProperty(localName = "FinInstnId")
    @NotNull
    @Valid
    private FinInstnId finInstnId;
}
