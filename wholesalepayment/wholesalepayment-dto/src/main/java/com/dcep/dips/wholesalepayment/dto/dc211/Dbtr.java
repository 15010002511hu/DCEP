/**
 * 
 */
package com.dcep.dips.wholesalepayment.dto.dc211;

import java.io.Serializable;
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
@JacksonXmlRootElement(localName = "Dbtr")
@Setter
@Getter
@ToString
public class Dbtr implements Serializable, DataEncryption {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3155814072576096222L;

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
