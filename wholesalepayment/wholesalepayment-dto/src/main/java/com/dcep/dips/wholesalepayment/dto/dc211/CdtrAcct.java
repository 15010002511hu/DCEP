package com.dcep.dips.wholesalepayment.dto.dc211;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JacksonXmlRootElement(localName = "CdtrAcct")
@Setter
@Getter
@ToString
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
	@NotNull
	@Valid
	private Tp tp;

	/**
	 * 收款人钱包名称
	 */
	@JacksonXmlProperty(localName = "Nm")
	@NotBlank
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
