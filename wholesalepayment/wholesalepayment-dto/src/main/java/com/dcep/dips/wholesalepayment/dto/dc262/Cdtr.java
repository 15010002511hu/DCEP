/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc262;

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
	 * 商户属性
	 */
	@JacksonXmlProperty(localName = "MrchntPrprty")
	@Pattern(regexp ="^[M][P][0-9]{2}")
	private String            mrchntPrprty;

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
	 * 商户英文简称
	 */
	@JacksonXmlProperty(localName = "MrchntEnAbbrNm")
	@Length(min = 1, max = 256)
	private String mrchntEnAbbrNm;

	/**
	 * 商户名称
	 */
	@JacksonXmlProperty(localName = "MrchntNm")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 60)
	private String mrchntNm;
	
	/**
     * 商户类别代码
     */
    @JacksonXmlProperty(localName = "MCC")
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mcc;

	/**
	 * 商户证件类型
	 */
	@JacksonXmlProperty(localName = "MrchntIdTp")
	@Pattern(regexp = "^[I][T][0-9]{2}")
	private String mrchntIdTp;

	/**
	 * 商户证件编码
	 */
	@JacksonXmlProperty(localName = "MrchntIdNo")
	@Length(min = 1, max = 32)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String mrchntIdNo;
	
	/**
	 * 受理订单号
	 */
	@JacksonXmlProperty(localName = "OutOrdrNo")
	@Length(min = 1, max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String outOrdrNo;

	/**
	 * 商户订单号
	 */
	@JacksonXmlProperty(localName = "MrchntOrdrNo")
	@Length(min = 1, max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String mrchntOrdrNo;

	/**
	 * 二维码类型 QT01：个人收款码 QT02：个人付款码 QT03：商户静态码 QT04：商户动态码
	 */
	@JacksonXmlProperty(localName = "QrTp")
	@Length(min = 4, max = 4)
	@Pattern(regexp = "^[Q][T][0-9]{2}")
	private String qrTp;

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
