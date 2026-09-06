/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc212;

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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author liuqi
 * @version $Id: OrgnlTxRef.java, v 0.1 2019年8月24日 下午2:59:46 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "OrgnlTxRef")
@Getter
@Setter
@ToString
public class OrgnlTxRef implements Serializable, DataEncryption{
	/**  */
    private static final long serialVersionUID = -934755193978449709L;
    /**
     * 原货币符号、结算金额
     */
    @JacksonXmlProperty(localName = "IntrBkSttlmAmt")
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount intrBkSttlmAmt;

    /**
     * PaymentTypeInformation
     */
    @JacksonXmlProperty(localName = "PmtTpInf")
    @NotNull
    @Valid
    private PmtTpInf pmtTpInf;
    
    /**
     * MandateRelatedInformation
     */
    @JacksonXmlProperty(localName = "MndtRltdInf")
    @NotNull
    @Valid
    private MndtRltdInf mndtRltdInf;

    /**
     * Debtor
     */
    @JacksonXmlProperty(localName = "Dbtr")
    @Valid
    private Dbtr dbtr;

    /**
     * DebtorAccount
     */
    @JacksonXmlProperty(localName = "DbtrAcct")
    @Valid
    private DbtrAcct dbtrAcct;

    /**
     * 数字货币
     * DigitalCurrency
     */
    @JacksonXmlProperty(localName = "DC")
    @Valid
    private DC dc;

    /**
     * 附言 RmtInf
     * RemittanceInformation
     */
    @JacksonXmlElementWrapper(localName = "RmtInf")
    @JacksonXmlProperty(localName = "Ustrd")
    @Valid
    private List<String> ustrds;

    /**
     * RemittanceInformation
     * 将ustrds转换为RmtInf对象
     */
    @Valid
    @JsonIgnore
    private RmtInf rmtInf;

//    /**
//     *
//     * RemittanceInformation
//     */
//    @JacksonXmlProperty(localName = "RmtInf")
//    @Valid
//    private RmtInf rmtInf;

    /**
     * 付款运营机构
     * DebtorAgent
     */
    @JacksonXmlProperty(localName = "DbtrAgt")
    @NotNull
    @Valid
    private DbtrAgt dbtrAgt;

    /**
     * 收款运营机构
     * CreditorAgent
     */
    @JacksonXmlProperty(localName = "CdtrAgt")
    @NotNull
    @Valid
    private CdtrAgt cdtrAgt;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if(dbtr != null) {
			dbtr.encryptData(encryptionHelper);
		}
		if(dbtrAcct != null) {
			dbtrAcct.encryptData(encryptionHelper);
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if(dbtr != null) {
			dbtr.decryptData(encryptionHelper);
		}
		if(dbtrAcct != null) {
			dbtrAcct.decryptData(encryptionHelper);
		}
	}

}
