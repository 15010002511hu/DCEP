/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc202;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
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
 * @version $Id: OrgnlTxRef.java, v 0.1 2019年8月24日 下午2:59:46 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "OrgnlTxRef")
@Getter
@Setter
@ToString
public class OrgnlTxRef implements Serializable, DataEncryption {/**  */
    private static final long serialVersionUID = -934755193978449709L;
    /**
     * 原货币符号、结算金额
     */
    @JacksonXmlProperty(localName = "IntrBkSttlmAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount intrBkSttlmAmt;
    /**
     * PaymentTypeInformation
     */
    @JacksonXmlProperty(localName = "PmtTpInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private PmtTpInf pmtTpInf;
    
    /**
     * MandateRelatedInformation
     */
    @JacksonXmlProperty(localName = "MndtRltdInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private MndtRltdInf mndtRltdInf;

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

//    /**
////     * RemittanceInformation
////     */
////    @JacksonXmlProperty(localName = "RmtInf")
////    @Valid
////    private RmtInf rmtInf;

    /**
     * DebtorAgent
     */
    @JacksonXmlProperty(localName = "DbtrAgt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private DbtrAgt dbtrAgt;
    /**
     * CreditorAgent
     */
    @JacksonXmlProperty(localName = "CdtrAgt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private CdtrAgt cdtrAgt;



    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        if (ustrds != null) {
            for (String ustrd : ustrds) {
                if (ustrd.contains("/CdtrNm/")) {
                    String encryptCdtrNm = encryptionHelper.encrypt(ustrd.substring("/CdtrNm/".length()));
                    ustrds.set(ustrds.indexOf(ustrd), "/CdtrNm/" + encryptCdtrNm);
                }
            }
        }
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        if (ustrds != null) {
            for (String ustrd : ustrds) {
                if (ustrd.contains("/CdtrNm/")) {
                    String decryptCdtrNm = encryptionHelper.decrypt(ustrd.substring("/CdtrNm/".length()));
                    ustrds.set(ustrds.indexOf(ustrd), "/CdtrNm/" + decryptCdtrNm);
                }
            }
        }
    }

}
