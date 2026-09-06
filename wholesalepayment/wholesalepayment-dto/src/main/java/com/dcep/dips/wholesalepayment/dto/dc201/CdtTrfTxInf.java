/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc201;

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
 * @author liuqi
 * @version $Id: CdtTrfTxInf.java, v 0.1 2019年8月23日 上午10:59:57 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "CdtTrfTxInf")
@Setter
@Getter
@ToString
public class CdtTrfTxInf implements Serializable, DataEncryption {

    /**
     * 
     */
    public CdtTrfTxInf() {
    }

    /**
     * @param pmtId
     * @param pmtTpInf
     * @param intrBkSttlmAmt
     * @param chrgBr
     * @param instgAgt
     * @param instdAgt
     * @param dbtr
     * @param dbtrAcct
     * @param cdtr
     * @param cdtrAcct
     * @param purp
     * @param rmtInf
     */
    public CdtTrfTxInf(PmtId pmtId, PmtTpInf pmtTpInf, ActiveCurrencyAndAmount intrBkSttlmAmt,
                       String chrgBr, InstgAgt instgAgt, InstdAgt instdAgt, Dbtr dbtr,
                       DbtrAcct dbtrAcct, Cdtr cdtr, CdtrAcct cdtrAcct, Purp purp, List<String> ustrds) {
        this.pmtId = pmtId;
        this.pmtTpInf = pmtTpInf;
        this.intrBkSttlmAmt = intrBkSttlmAmt;
        this.chrgBr = chrgBr;
        this.instgAgt = instgAgt;
        this.instdAgt = instdAgt;
        this.dbtr = dbtr;
        this.dbtrAcct = dbtrAcct;
        this.cdtr = cdtr;
        this.cdtrAcct = cdtrAcct;
        this.purp = purp;
        this.ustrds = ustrds;
//        this.rmtInf = rmtInf;
    }

    /**  */
    private static final long       serialVersionUID = -6638551256104252471L;

    /**
     * PaymentIdentificat
     */
    @JacksonXmlProperty(localName = "PmtId")
    @NotNull
    @Valid
    private PmtId                   pmtId;

    /**
     * PaymentTypeInformation
     */
    @JacksonXmlProperty(localName = "PmtTpInf")
    @NotNull
    @Valid
    private PmtTpInf                pmtTpInf;

    /**
     * PaymentTypeInformation
     */
    @JacksonXmlProperty(localName = "IntrBkSttlmAmt")
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount intrBkSttlmAmt;

    /**
     * IntrBkSttlmDt 期望结算日期
     */
    @JacksonXmlProperty(localName = "IntrBkSttlmDt")
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd")
    private String                  intrBkSttlmDt;

    /**
     * SttlmPrty 业务优先级
     */
    @JacksonXmlProperty(localName = "SttlmPrty")
    @NotBlank
    @Pattern(regexp = "URGT|HIGH|NORM")
    private String                  sttlmPrty;

    /**
     * ChargeBearer
     */
    @JacksonXmlProperty(localName = "ChrgBr")
    @NotBlank
    @Pattern(regexp = "DEBT")
    private String                  chrgBr;

    /**
     *  InstructingAgent
     */
    @JacksonXmlProperty(localName = "InstgAgt")
    @Valid
    @NotNull
    private InstgAgt                instgAgt;

    /**
     * InstructedAgent
     */
    @JacksonXmlProperty(localName = "InstdAgt")
    @Valid
    @NotNull
    private InstdAgt                instdAgt;

    /**
     * Debtor
     */
    @JacksonXmlProperty(localName = "Dbtr")
    @Valid
    private Dbtr                    dbtr;

    /**
     * DebtorAccount
     */
    @JacksonXmlProperty(localName = "DbtrAcct")
    @Valid
    @NotNull
    private DbtrAcct                dbtrAcct;

    /**
     * Creditor
     */
    @JacksonXmlProperty(localName = "Cdtr")
    @Valid
    private Cdtr                    cdtr;

    /**
     * DebtorAccount
     */
    @JacksonXmlProperty(localName = "CdtrAcct")
    @Valid
    @NotNull
    private CdtrAcct                cdtrAcct;

    /**
     * Purpose
     */
    @JacksonXmlProperty(localName = "Purp")
    @Valid
    @NotNull
    private Purp                    purp;

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
    private RmtInf                  rmtInf;

    @JacksonXmlProperty(localName = "SplmtryData")
    @Valid
    private SplmtryData splmtryData;


	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		// 加密
		if(dbtr != null) {
			dbtr.encryptData(encryptionHelper);
		}
		if(dbtrAcct != null) {
			dbtrAcct.encryptData(encryptionHelper);
		}
		if(cdtr != null) {
			cdtr.encryptData(encryptionHelper);
		}
		if(cdtrAcct != null) {
			cdtrAcct.encryptData(encryptionHelper);
		}
		if (ustrds != null) {
			for (String ustrd : ustrds) {
	            if (ustrd.contains("/MaskedTel/")) {
	                String encryptMaskedTel = encryptionHelper.encrypt(ustrd.substring("/MaskedTel/".length()));
	                ustrds.set(ustrds.indexOf(ustrd), "/MaskedTel/" + encryptMaskedTel);
	            } else if (ustrd.contains("/MaskedEmail/")) {
	            	String encryptMaskedEmail = encryptionHelper.encrypt(ustrd.substring("/MaskedEmail/".length()));
	            	ustrds.set(ustrds.indexOf(ustrd), "/MaskedEmail/" + encryptMaskedEmail);
	            }
	        }
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		// 解密
		if(dbtr != null) {
			dbtr.decryptData(encryptionHelper);
		}
		if(dbtrAcct != null) {
			dbtrAcct.decryptData(encryptionHelper);
		}
		if(cdtr != null) {
			cdtr.decryptData(encryptionHelper);
		}
		if(cdtrAcct != null) {
			cdtrAcct.decryptData(encryptionHelper);
		}
		if (ustrds != null) {
			for (String ustrd : ustrds) {
	            if (ustrd.contains("/MaskedTel/")) {
	                String decryptMaskedTel = encryptionHelper.decrypt(ustrd.substring("/MaskedTel/".length()));
	                ustrds.set(ustrds.indexOf(ustrd), "/MaskedTel/" + decryptMaskedTel);
	            } else if (ustrd.contains("/MaskedEmail/")) {
	            	String decryptMaskedEmail = encryptionHelper.decrypt(ustrd.substring("/MaskedEmail/".length()));
	            	ustrds.set(ustrds.indexOf(ustrd), "/MaskedEmail/" + decryptMaskedEmail);
	            }
	        }
		}
	}

}
