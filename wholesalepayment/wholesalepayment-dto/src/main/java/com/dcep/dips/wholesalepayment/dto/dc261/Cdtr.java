/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc261;

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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author chenxf
 * @version $Id: Cdtr.java
 */
@JacksonXmlRootElement(localName = "Cdtr")
@Setter
@Getter
@ToString
public class Cdtr implements Serializable, DataEncryption {
	/**  */
	private static final long serialVersionUID = -195955243492995184L;

	/**
	 * 收款钱包名称 允许中文
	 */
	@JacksonXmlProperty(localName = "CdtrWltNm")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 60)
	private String cdtrWltNm;

	/**
	 * 收款钱包ID
	 */
	@JacksonXmlProperty(localName = "CdtrWltId")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 16, max = 16)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String cdtrWltId;

	/**
	 * 收款钱包类型
	 */
	@JacksonXmlProperty(localName = "CdtrWltTp")
	@Length(min = 4, max = 4)
	@Pattern(regexp = "WT01||WT02||WT09||WT10")
	private String cdtrWltTp;

	/**
	 * 收款钱包等级
	 */
	@JacksonXmlProperty(localName = "CdtrWltLvl")
	@Length(min = 4, max = 4)
	@Pattern(regexp = "WL01||WL02||WL03||WL04||WL05")
	private String cdtrWltLvl;

	/**
	 * 收款运营机构
	 */
	@JacksonXmlProperty(localName = "CdtrPtyId")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 14, max = 14)
	@Pattern(regexp = "^[0-9a-zA-Z]{14}$")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@CheckOrgState(groups = Priority.Lowest.class)
	private String cdtrPtyId;

	/**
	 * 商户号
	 */
	@JacksonXmlProperty(localName = "MrchntNo")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 35)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String mrchntNo;

	/**
	 * 商户简称
	 */
	@JacksonXmlProperty(localName = "MrchntAbbrNm")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 30)
	private String mrchntAbbrNm;

	/**
	 * 商户名称
	 */
	@JacksonXmlProperty(localName = "MrchntNm")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 60)
	private String mrchntNm;
	
	/**
     * 【收款方优惠活动信息】
     */
    @JacksonXmlProperty(localName = "CdtrPrmtInf")
    @Valid
	private CdtrPrmtInf cdtrPrmtInf;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if (cdtrWltId != null) {
			this.setCdtrWltId(encryptionHelper.encrypt(cdtrWltId));
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if (cdtrWltId != null) {
			this.setCdtrWltId(encryptionHelper.decrypt(cdtrWltId));
		}
	}

}
