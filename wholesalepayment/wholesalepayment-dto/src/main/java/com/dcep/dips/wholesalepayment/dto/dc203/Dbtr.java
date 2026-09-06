/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc203;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

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
@AllArgsConstructor
@NoArgsConstructor
public class Dbtr implements Serializable, DataEncryption {
	/**  */
	private static final long serialVersionUID = -2000794055141875938L;

	/**
	 * 付款人名称
	 */
	@JacksonXmlProperty(localName = "Nm")
	@Length(min = 0, max = 60)
	private String nm;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		// 加密字段
		if(nm != null) {
			this.setNm(encryptionHelper.encrypt(nm));
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		// 解密字段
		if(nm != null) {
			this.setNm(encryptionHelper.decrypt(nm));
		}
	}

}
