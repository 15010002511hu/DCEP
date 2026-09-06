/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc201;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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
 * @version $Id: CdtrAcct.java, v 0.1 2019年8月23日 下午8:07:40 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "CdtrAcct")
@Setter
@Getter
@ToString
public class CdtrAcct implements Serializable, DataEncryption {
    /**  */
    private static final long serialVersionUID = -6170273968778944748L;

    /**
     * Identification
     */
    @JacksonXmlProperty(localName = "Id")
    @Valid
    @NotNull
    private Id                id;
    /**
     * type
     */
    @JacksonXmlProperty(localName = "Tp")
    @Valid
    private Tp                tp;
    /**
     * 收款人钱包名称 允许中文
     */
    @JacksonXmlProperty(localName = "Nm")
    @Length(min = 0, max = 60)
    private String            nm;
    
	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if(id != null) {
			id.encryptData(encryptionHelper);
		}
	}
	
	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if(id != null) {
			id.decryptData(encryptionHelper);
		}
	}

}
