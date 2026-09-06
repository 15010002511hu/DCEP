package com.dcep.supergw.dto.dc631;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
	private String transactionIndex;
	
	/**
	 * 离线交易时间
	 */
	@JacksonXmlProperty(localName = "TranTms")
	@NotBlank
	@Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
	private String tranTms;
	
	/**
	 * 交易金额
	 */
	@JacksonXmlProperty(localName = "Amount")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount amount;
	
	/**
	 * 付款方最近同步时间
	 */
	@JacksonXmlProperty(localName = "TranSyncTime")
	@NotBlank
	@Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
	private String tranSyncTime;

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
