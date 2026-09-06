package com.dcep.dips.wholesalepayment.dto.dc211;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * 
 * @author sunxiaofeng
 * @version $Id: DrctDbtTxInf.java, v 0.1 2019年10月15日 上午11:13:14 sunxiaofeng Exp
 *          $
 */
@JacksonXmlRootElement(localName = "DrctDbtTxInf")
@Setter
@Getter
@ToString
public class DrctDbtTxInf implements Serializable, DataEncryption {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2104859501032188868L;

	@JacksonXmlProperty(localName = "PmtId")
	@NotNull
	@Valid
	private PmtId pmtId;

	@JacksonXmlProperty(localName = "PmtTpInf")
	@NotNull
	@Valid
	private PmtTpInf pmtTpInf;

	@JacksonXmlProperty(localName = "IntrBkSttlmAmt")
	@NotNull
	@Valid
	private ActiveCurrencyAndAmount intrBkSttlmAmt;

	@JacksonXmlProperty(localName = "ChrgBr")
	@NotBlank
	@Pattern(regexp = "CRED")
	private String chrgBr;

	@JacksonXmlProperty(localName = "Cdtr")
	@Valid
	private Cdtr cdtr;

	@JacksonXmlProperty(localName = "CdtrAcct")
	@NotNull
	@Valid
	private CdtrAcct cdtrAcct;

	@JacksonXmlProperty(localName = "CdtrAgt")
	@NotNull
	@Valid
	private CdtrAgt cdtrAgt;

	@JacksonXmlProperty(localName = "Dbtr")
	@Valid
	private Dbtr dbtr;

	@JacksonXmlProperty(localName = "DbtrAcct")
	@Valid
	private DbtrAcct dbtrAcct;

	@JacksonXmlProperty(localName = "DbtrAgt")
	@NotNull
	@Valid
	private DbtrAgt dbtrAgt;

	@JacksonXmlProperty(localName = "Purp")
	@NotNull
	@Valid
	private Purp purp;

	@JacksonXmlProperty(localName = "SplmtryData")
	@Valid
	private SplmtryData splmtryData;

    /**
     * 附言 RmtInf
     * RemittanceInformation
     */
    @JacksonXmlElementWrapper(localName = "RmtInf")
    @JacksonXmlProperty(localName = "Ustrd")
    @NotNull
    @Valid
    private List<String> ustrds;

	/**
	 * RemittanceInformation
	 * 将ustrds转换为RmtInf对象
	 */
	@Valid
	@JsonIgnore
	private RmtInf                  rmtInf;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if(cdtr != null) {
			cdtr.encryptData(encryptionHelper);
		}
		if(cdtrAcct != null) {
			cdtrAcct.encryptData(encryptionHelper);
		}
		if(dbtr != null) {
			dbtr.encryptData(encryptionHelper);
		}
		if(dbtrAcct != null) {
			dbtrAcct.encryptData(encryptionHelper);
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if(cdtr != null) {
			cdtr.decryptData(encryptionHelper);
		}
		if(cdtrAcct != null) {
			cdtrAcct.decryptData(encryptionHelper);
		}
		if(dbtr != null) {
			dbtr.decryptData(encryptionHelper);
		}
		if(dbtrAcct != null) {
			dbtrAcct.decryptData(encryptionHelper);
		}
	}

//	@JacksonXmlProperty(localName = "RmtInf")
//	@NotNull
//	private RmtInf rmtInf;

}
