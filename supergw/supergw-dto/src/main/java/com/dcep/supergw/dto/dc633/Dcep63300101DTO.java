package com.dcep.supergw.dto.dc633;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 	脱机交易状态查询报文<dcep.633.001.01>
 * 
 * @author duzhong
 * @version : Dcep63300101DTO.java v 0.1 2020-09-07
 * @description 通过本报文，付款方运营机构发送“处理中”交易至收款方运营机构，进行交易终态处理。
 */
@JacksonXmlRootElement(localName = "OfflineTxQuery", namespace = "http://www.dcep.com/dcep/63300101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.633.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
public class Dcep63300101DTO extends GwDTO  implements DataEncryption{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6474065051789290698L;

	/**
	 * 【业务头组件】
	 */
	@JacksonXmlProperty(localName = "GrpHdr")
	@NotNull(groups = Priority.Highest.class)
	@CheckGrpHdrOrgId(groups = Priority.Lowest.class)
	@CheckGrpHdrMsgId(groups = Priority.Lowest.class)
	@Valid
	private GrpHdr grpHdr;

	/**
	 * 个人化申请信息
	 */
	@NotNull
	@Valid
	@JacksonXmlElementWrapper(localName = "TransactionList")
    @JacksonXmlProperty(localName = "TransactionInfo")
	private List<TransactionInfo> transactionList;

	@Override
	public void init() {

	}

	@Override
	public String fetchMsgId() {
		return grpHdr.getMsgId();
	}

	@Override

	public boolean check(SoapHeader header) {
		return CheckUtils.requestMsgChk(header, grpHdr);
	}


	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		List<String> plainTextList =  fetchSecretFactor();
		List<String> cipherTextList =  encryptionHelper.encrypt(plainTextList);
		secretAssign(cipherTextList);
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		List<String> cipherTextList =  fetchSecretFactor();
		List<String> plainTextList =  encryptionHelper.decrypt(cipherTextList);
		secretAssign(plainTextList);
	}

	public List<String> fetchSecretFactor() {
		List<String> result = new ArrayList<>();
		if (this.transactionList != null && !transactionList.isEmpty()) {
			for (TransactionInfo transactionInfo : transactionList) {
				if (transactionInfo.getDebtorWalletId() != null) {
					result.add(transactionInfo.getDebtorWalletId());
				} if (transactionInfo.getCreditorWalletId() != null) {
					result.add(transactionInfo.getCreditorWalletId());
				}
			}
		}
		return result;
	}

	public void secretAssign(List<String> secretList) {
		if (transactionList != null && !transactionList.isEmpty()) {
			int index = 0;
			for (int i = 0; i < transactionList.size(); i++) {
				if (transactionList.get(i).getDebtorWalletId() != null) {
					transactionList.get(i).setDebtorWalletId(secretList.get(index++));
				}
				if (transactionList.get(i).getCreditorWalletId() != null) {
					transactionList.get(i).setCreditorWalletId(secretList.get(index++));
				}
			}
		}
	}
}
