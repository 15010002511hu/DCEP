package com.dcep.dips.wholesalepayment.dto.dc21302;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JacksonXmlRootElement(localName = "Id")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Id implements Serializable, DataEncryption {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6240031675109370056L;
	@JacksonXmlProperty(localName = "Othr")
	@NotNull
	@Valid
	private Othr othr;
	
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
