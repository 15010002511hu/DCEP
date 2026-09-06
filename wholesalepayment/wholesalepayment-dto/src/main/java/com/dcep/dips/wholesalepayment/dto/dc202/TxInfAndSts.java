/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc202;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import org.hibernate.validator.constraints.Length;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author liuqi
 * @version $Id: TxInfAndSts.java, v 0.1 2019年8月24日 下午2:41:07 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "TxInfAndSts")
@Getter
@Setter
@ToString
public class TxInfAndSts implements Serializable, DataEncryption {
    /**  */
    private static final long serialVersionUID = 4292900250422854061L;
    /**
     * 业务回执状态
     */
    @JacksonXmlProperty(localName = "StsId")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 4, max = 4)
    @Pattern(regexp = "PR00||PR01")
    private String            stsId;
    /**
     *  交易批次号 
     */
    @JacksonXmlProperty(localName = "OrnlInstrId")
    @Pattern(regexp = "^[B][0-9]{12}$")
    private String            ornlInstrId;

    /**
     * 原明细标识号
     */
    @JacksonXmlProperty(localName = "OrgnlTxId")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 32, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            orgnlTxId;

    /**
     * StatusReasonInformation
     */
    @JacksonXmlProperty(localName = "StsRsnInf")
    @Valid
    private StsRsnInfTxn      stsRsnInf;
    /**
     * OriginalTransactionReference
     */
    @JacksonXmlProperty(localName = "OrgnlTxRef")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private OrgnlTxRef        orgnlTxRef;

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        if (orgnlTxRef != null) {
            orgnlTxRef.encryptData(encryptionHelper);
        }
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        if (orgnlTxRef != null) {
            orgnlTxRef.decryptData(encryptionHelper);
        }
    }

}
