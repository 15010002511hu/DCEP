package com.dcep.supergw.dto.dc633;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 待处理交易列表信息
 * 
 * @version : TransactionInfo.java v 0.1 2020-09-07
 * @author duzhong
 */

@Getter
@Setter
@ToString
public class TransactionInfo implements Serializable, DataEncryption {
	/**
	* 
	*/
	private static final long serialVersionUID = 6486414988966422176L;

	/**
	 * 付款硬钱包Id
	 */
	@JacksonXmlProperty(localName = "DebtorWalletId")
	@NotBlank
	@Length(min = 1, max = 34)
	private String debtorWalletId;

	/**
	 * 收款硬钱包Id
	 */
	@JacksonXmlProperty(localName = "CreditorWalletId")
	@NotBlank
	@Length(min = 1, max = 34)
	private String creditorWalletId;

	/**
	 * 交易索引
	 */
	@JacksonXmlProperty(localName = "TransactionIndex")
	@NotBlank
	@Length(min = 1, max = 32)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String transactionIndex;
	

	@JacksonXmlProperty(localName = "TransactionStatus")
	@Pattern(regexp = "00||01")
	@NotBlank
	private String transactionStatus;
	


	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if (this.getDebtorWalletId() != null) {
			 this.setDebtorWalletId(encryptionHelper.encrypt(this.getDebtorWalletId()));
		}
		if (this.getCreditorWalletId() != null) {
			 this.setCreditorWalletId(encryptionHelper.encrypt(this.getCreditorWalletId()));
		}
		
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if (this.getDebtorWalletId() != null) {
			this.setDebtorWalletId(encryptionHelper.decrypt(this.getDebtorWalletId()));
		}
		if (this.getCreditorWalletId() != null) {
			this.setCreditorWalletId(encryptionHelper.decrypt(this.getCreditorWalletId()));
		}
	}

	

}
