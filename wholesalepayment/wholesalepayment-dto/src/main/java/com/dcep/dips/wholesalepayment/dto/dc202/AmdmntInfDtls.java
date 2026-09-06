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
 * @version $Id: AmdmntInfDtls.java, v 0.1 2019年8月24日 下午3:30:49 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "AmdmntInfDtls")
@Getter
@Setter
@ToString
public class AmdmntInfDtls implements Serializable{/**  */
    private static final long serialVersionUID = -342396636921185459L;
    /**
     * OriginalDebtorAgent
     */
    @JacksonXmlProperty(localName = "OrgnlDbtrAgt")
    @NotNull
    @Valid
    private OrgnlDbtrAgt orgnlDbtrAgt;
}
