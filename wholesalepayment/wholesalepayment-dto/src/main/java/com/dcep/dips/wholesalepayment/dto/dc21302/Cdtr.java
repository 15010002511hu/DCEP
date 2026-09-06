package com.dcep.dips.wholesalepayment.dto.dc21302;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@JacksonXmlRootElement(localName = "Cdtr")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Cdtr implements Serializable, DataEncryption {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5884958617915048763L;

	/**
	 * 付款人名称
	 */
	@JacksonXmlProperty(localName = "Nm")
	@Length(min = 1, max = 60)
	private String nm;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if(nm != null) {
			this.setNm(encryptionHelper.encrypt(nm));
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if(nm != null) {
			this.setNm(encryptionHelper.decrypt(nm));
		}
	}
}
