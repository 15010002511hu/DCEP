/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc212;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * 
 * @author liuqi
 * @version $Id: Dbtr.java, v 0.1 2019年8月23日 下午7:47:37 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "Dbtr")
@Getter
@Setter
@ToString
public class Dbtr implements Serializable, DataEncryption{

    /**  */
    private static final long serialVersionUID = -2000794055141875938L;

    /*
     * Party
     */
    @JacksonXmlProperty(localName = "Pty")
    @Valid
    private Pty pty;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if(pty != null) {
			pty.encryptData(encryptionHelper);
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if(pty != null) {
			pty.decryptData(encryptionHelper);
		}
	}
    
}
