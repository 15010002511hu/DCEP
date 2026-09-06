/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc201;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author liuqi
 * @version $Id: Id.java, v 0.1 2019年8月23日 下午7:50:53 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "Id")
@Getter
@Setter
@ToString
public class Id implements Serializable, DataEncryption {

    /**  */
    private static final long serialVersionUID = 1971134699546226409L;
    /**
     * Identification
     */
    @JacksonXmlProperty(localName = "Othr")
    @Valid
    @NotNull
    private Othr              othr;
    
	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if(othr != null) {
			othr.encryptData(encryptionHelper);
		}
	}
	
	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if(othr != null) {
			othr.decryptData(encryptionHelper);
		}
	}
}
