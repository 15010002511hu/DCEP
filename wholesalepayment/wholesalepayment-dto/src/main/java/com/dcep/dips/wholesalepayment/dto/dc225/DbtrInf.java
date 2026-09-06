/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc225;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckOrgState;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author liuqi
 * @version $Id: DbtrInf.java, v 0.1 2019年8月22日 下午4:26:11 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "DbtrInf")
@Setter
@Getter
@ToString
public class DbtrInf implements Serializable,DataEncryption {
    /**  */
    private static final long serialVersionUID = 6554616528096429293L;

    /**
     * 付款人钱包所属运营机构
     */
    @JacksonXmlProperty(localName = "DbtrPtyId")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 14, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @CheckOrgState(groups = Priority.Lowest.class)
    private String            dbtrPtyId;

    /**
     * 付款人账户类型
     */
    @JacksonXmlProperty(localName = "DbtAcctTp")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "^[A][T][0-9]{2}")
    private String            dbtAcctTp;

    /**
     * 付款人名称
     */
    @JacksonXmlProperty(localName = "DbtrNm")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 60)
    private String            dbtrNm;

    /**
     * 付款人账户账号
     */
    @JacksonXmlProperty(localName = "DbtrAcct")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            dbtrAcct;

    /**
     * 挂接协议号
     */
    @JacksonXmlProperty(localName = "SgnNo")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            sgnNo;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if(dbtrNm != null) {
			this.setDbtrNm(encryptionHelper.encrypt(dbtrNm));
		}
		if(dbtrAcct != null) {
			this.setDbtrAcct(encryptionHelper.encrypt(dbtrAcct));
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if(dbtrNm != null) {
			this.setDbtrNm(encryptionHelper.decrypt(dbtrNm));
		}
		if(dbtrAcct != null) {
			this.setDbtrAcct(encryptionHelper.decrypt(dbtrAcct));
		}
	}

}
