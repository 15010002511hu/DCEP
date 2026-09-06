/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc20302;

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
 * 
 * @author liuqi
 * @version $Id: DbtrAcct.java, v 0.1 2019年8月23日 下午7:49:48 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "DbtrAcct")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DbtrAcct implements Serializable, DataEncryption {
    /**  */
    private static final long serialVersionUID = -8292986746038149377L;

    /**
     * Identification
     */
    @JacksonXmlProperty(localName = "Id")
    @Valid
    @NotNull
    private Id id;
    /**
     * type
     */
    @JacksonXmlProperty(localName = "Tp")
    @Valid
    private Tp tp;

    /**
     * 付款人钱包名称
     */
    @JacksonXmlProperty(localName = "Nm")
    @Length(min = 0, max = 60)
    private String            nm;

    /**
     * DigitalCurrency
     */
    @JacksonXmlProperty(localName = "DC")
    @Valid
    @NotNull
    private DC dc;

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
