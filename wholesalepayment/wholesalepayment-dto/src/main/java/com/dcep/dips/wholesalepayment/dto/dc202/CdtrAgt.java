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
 * @version $Id: CdtrAgt.java, v 0.1 2019年8月24日 下午3:39:58 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "CdtrAgt")
@Getter
@Setter
@ToString
public class CdtrAgt implements Serializable{/**  */
    private static final long serialVersionUID = 454735525303720289L;
    /**
     * FinancialInstitutionIdentification
     */
    @JacksonXmlProperty(localName = "FinInstnId")
    @NotNull
    @Valid
    private FinInstnId finInstnId;
}
