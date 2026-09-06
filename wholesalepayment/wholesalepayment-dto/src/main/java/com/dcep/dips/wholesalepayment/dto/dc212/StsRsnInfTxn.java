/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc212;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * 
 * @author chenkai
 * @version $Id: StsRsnInfTxn.java, v 0.1 2019年10月15日 下午2:50:46 chenkai Exp $
 */
@JacksonXmlRootElement(localName = "StsRsnInf")
@Getter
@Setter
@ToString
public class StsRsnInfTxn implements Serializable{/**  */
    private static final long serialVersionUID = 679906241775185894L;
    /**
     * Reason业务拒绝码
     */
    @JacksonXmlProperty(localName = "Rsn")
    @Valid
    private Rsn rsn;
    /**
     * 业务拒绝原因 允许中文
     */
    @JacksonXmlProperty(localName = "AddtlInf")
    @Length(min = 1, max = 105)
    private String addtlInf;
}
