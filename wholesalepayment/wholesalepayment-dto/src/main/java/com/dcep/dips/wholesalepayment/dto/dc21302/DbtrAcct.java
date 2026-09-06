/**
 * 
 */
package com.dcep.dips.wholesalepayment.dto.dc21302;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author sunxiaofeng
 *
 */
@JacksonXmlRootElement(localName = "DbtrAcct")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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

	/**
	 * 付款人链上账户地址
	 */
	@JacksonXmlProperty(localName = "Prxy")
	@NotNull
	@Valid
	private Prxy prxy;

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
