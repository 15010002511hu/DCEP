/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc281;

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
public class CdtrInf implements Serializable, DataEncryption {
    /**  */
    private static final long serialVersionUID = -7732034558775980582L;

    /*
     * 收款人账户所属运营机构
     */
    @JacksonXmlProperty(localName = "CdtrPtyId")
    @NotBlank
    @Length(min = 14, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @CheckOrgState(groups = Priority.Lowest.class)
    private String            cdtrPtyId;

    /*
     * 收款人名称
     */
    @JacksonXmlProperty(localName = "CdtrNm")
    @Length(min = 1, max = 60)
    private String            cdtrNm;

    /**
     * 收款钱包ID
     */
    @JacksonXmlProperty(localName = "CdtrWltId")
    @Length(min = 16, max = 16)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            cdtrWltId;

    /*
     * 收款人钱包名称
     */
    @JacksonXmlProperty(localName = "CdtrWltNm")
    @Length(min = 1, max = 60)
    private String            cdtrWltNm;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if(cdtrNm != null) {
			this.setCdtrNm(encryptionHelper.encrypt(cdtrNm));
		}
		if(cdtrWltId != null) {
			this.setCdtrWltId(encryptionHelper.encrypt(cdtrWltId));
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if(cdtrNm != null) {
			this.setCdtrNm(encryptionHelper.decrypt(cdtrNm));
		}
		if(cdtrWltId != null) {
			this.setCdtrWltId(encryptionHelper.decrypt(cdtrWltId));
		}
	}

}
