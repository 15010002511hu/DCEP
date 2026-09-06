package com.dcep.dips.wholesalepayment.dto.dc213;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JacksonXmlRootElement(localName = "CdtrAcct")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CdtrAcct implements Serializable, DataEncryption {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5980980451980505707L;
	@JacksonXmlProperty(localName = "Id")
	@NotNull
	@Valid
	private Id id;

	@JacksonXmlProperty(localName = "Tp")
	@Valid
	private Tp tp;

	/**
	 * 收款人钱包名称
	 */
	@JacksonXmlProperty(localName = "Nm")
	@Length(min = 1, max = 60)
	private String nm;

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
