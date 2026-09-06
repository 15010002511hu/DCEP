/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc221;

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
 * @version $Id: CdtrInf.java, v 0.1 2019年8月22日 下午4:26:30 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "CdtrInf")
@Setter
@Getter
@ToString
public class CdtrInf implements Serializable,DataEncryption {
    /**  */
    private static final long serialVersionUID = -7732034558775980582L;

    /*
     * 收款人账户所属运营机构
     */
    @JacksonXmlProperty(localName = "CdtrPtyId")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 14, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @CheckOrgState(groups = Priority.Lowest.class)
    private String            cdtrPtyId;

    /*
     * 收款人账户类型
     */
    @JacksonXmlProperty(localName = "CdtrAcctTp")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 4, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "^[A][T][0-9]{2}")
    private String            cdtrAcctTp;

    /*
     * 收款人名称
     */
    @JacksonXmlProperty(localName = "CdtrNm")
    @NotBlank
    @Length(min = 1, max = 60)
    private String            cdtrNm;

    /*
     * 收款人账户账号
     */
    @JacksonXmlProperty(localName = "CdtrAcct")
    @NotBlank
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            cdtrAcct;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if(cdtrNm != null) {
			this.setCdtrNm(encryptionHelper.encrypt(cdtrNm));
		}
		if(cdtrAcct != null) {
			this.setCdtrAcct(encryptionHelper.encrypt(cdtrAcct));
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if(cdtrNm != null) {
			this.setCdtrNm(encryptionHelper.decrypt(cdtrNm));
		}
		if(cdtrAcct != null) {
			this.setCdtrAcct(encryptionHelper.decrypt(cdtrAcct));
		}
	}

}
