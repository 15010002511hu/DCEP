/**
 * 
 */
package com.dcep.dips.wholesalepayment.dto.dc211;

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
 * @author sunxiaofeng
 *
 */
@JacksonXmlRootElement(localName = "DbtrAcct")
@Setter
@Getter
@ToString
public class DbtrAcct implements Serializable, DataEncryption {

	/**
	 * 
	 */
	private static final long serialVersionUID = 306076255826865293L;

	@JacksonXmlProperty(localName = "Id")
	@NotNull
	@Valid
	private Id id;

	@JacksonXmlProperty(localName = "Tp")
	@Valid
	private Tp tp;

	/**
	 * 付款人钱包名称
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
