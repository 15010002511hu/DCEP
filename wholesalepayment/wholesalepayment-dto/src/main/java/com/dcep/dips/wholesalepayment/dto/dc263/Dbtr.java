/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc263;

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
 * @author chenxf
 * @version $Id: Dbtr.java
 */
@JacksonXmlRootElement(localName = "Dbtr")
@Setter
@Getter
@ToString
public class Dbtr implements Serializable, DataEncryption {
	/**  */
	private static final long serialVersionUID = 5116147351803361659L;
	/**
	 * 付款钱包名称 允许中文
	 */
	@JacksonXmlProperty(localName = "DbtrWltNm")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 60)
	private String dbtrWltNm;

	/**
	 * 付款钱包ID
	 */
	@JacksonXmlProperty(localName = "DbtrWltId")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 16, max = 16)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String dbtrWltId;

	/**
	 * 付款钱包类型
	 */
	@JacksonXmlProperty(localName = "DbtrWltTp")
	@Length(min = 4, max = 4)
	@Pattern(regexp = "WT01||WT02||WT09||WT10")
	private String dbtrWltTp;

	/**
	 * 付款钱包等级
	 */
	@JacksonXmlProperty(localName = "DbtrWltLvl")
	@Length(min = 4, max = 4)
	@Pattern(regexp = "WL01||WL02||WL03||WL04||WL05")
	private String dbtrWltLvl;
	/**
	 * 付款运营机构
	 */
	@JacksonXmlProperty(localName = "DbtrPtyId")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 14, max = 14)
	@Pattern(regexp = "^[0-9a-zA-Z]{14}$")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@CheckOrgState(groups = Priority.Lowest.class)
	private String dbtrPtyId;
	
	/**
     * 居民类型
     */
    @JacksonXmlProperty(localName = "ResdtTp")
    @Pattern(regexp = "^[R][T][0-9]{2}||REST[0-9]{2}")
    private String            resdtTp;
    
    /**
     * 常驻国家/地区代码
     */
    @JacksonXmlProperty(localName = "ResdtCtryCd")
    @Length(min = 1, max = 3)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            resdtCtryCd;
    
    /**
     * 钱包注册手机号所在国家/地区代码
     */
    @JacksonXmlProperty(localName = "RegrCtryCd")
    @Length(min = 1, max = 3)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            regrCtryCd;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if (dbtrWltId != null) {
			this.setDbtrWltId(encryptionHelper.encrypt(dbtrWltId));
		}
		if (dbtrWltNm != null) {
			this.setDbtrWltNm(encryptionHelper.encrypt(dbtrWltNm));
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if (dbtrWltId != null) {
			this.setDbtrWltId(encryptionHelper.decrypt(dbtrWltId));
		}
		if (dbtrWltNm != null) {
			this.setDbtrWltNm(encryptionHelper.decrypt(dbtrWltNm));
		}
	}
}
