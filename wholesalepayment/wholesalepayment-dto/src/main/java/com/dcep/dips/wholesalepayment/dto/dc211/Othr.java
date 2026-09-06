package com.dcep.dips.wholesalepayment.dto.dc211;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
 * @author sunxiaofeng
 *
 */
@JacksonXmlRootElement(localName = "Othr")
@Setter
@Getter
@ToString
public class Othr implements Serializable, DataEncryption {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1713473992985701401L;
	/*
	 * 付款人钱包ID/收款人钱包ID
	 */
	@JacksonXmlProperty(localName = "Id")
	@NotBlank
	@Length(min = 16, max = 16)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String id;

	/**
	 * SchemeName
	 */
	@JacksonXmlProperty(localName = "SchmeNm")
	@Valid
	private SchmeNm schmeNm;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if(id != null) {
			this.setId(encryptionHelper.encrypt(id));
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if(id != null) {
			this.setId(encryptionHelper.decrypt(id));
		}
	}
}
