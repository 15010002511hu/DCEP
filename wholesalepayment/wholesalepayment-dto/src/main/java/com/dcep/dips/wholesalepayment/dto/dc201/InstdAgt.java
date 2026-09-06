/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc201;

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
 * @version $Id: InstdAgt.java, v 0.1 2019年8月23日 下午7:46:13 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "InstdAgt")
@Getter
@Setter
@ToString
public class InstdAgt implements Serializable {

    /**  */
    private static final long serialVersionUID = 3937604156122889075L;
    /**
     * FinancialInstitutionIdentification
     */
    @JacksonXmlProperty(localName = "FinInstnId")
    @Valid
    @NotNull
    private FinInstnId        finInstnId;
}
