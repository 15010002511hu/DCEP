package com.dcep.supergw.dto.dc441;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author : duzhong
 * @version : 2020-04-02
 * @description :
 */
@Getter
@Setter
@ToString
public class PtcInf implements Serializable, DataEncryption {

	private static final long serialVersionUID = 4853065045747805741L;

	/**
	 * 签约协议号
	 */
	@JacksonXmlProperty(localName = "PtcId")
	@Length(min = 1, max = 34)
	private String ptcId;

	@JacksonXmlProperty(localName = "SgnChnl")
	@NotBlank
	@Pattern(regexp = "SC00||SC01||SC02")
	private String sgnChnl;

	@JacksonXmlProperty(localName = "SgnAcctPtyId")
	@NotBlank
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Length(min = 1, max = 14)
	private String sgnAcctPtyId;

	@JacksonXmlProperty(localName = "SgnAcctTp")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Pattern(regexp = "AT00||AT01||AT02||AT03||AT04||AT05||AT06||AT07")
	private String sgnAcctTp;

	@JacksonXmlProperty(localName = "SgnAcctId")
	@Length(min = 1, max = 34)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String sgnAcctId;

	@JacksonXmlProperty(localName = "SgnAcctNm")
	@Length(min = 1, max = 60)
	private String sgnAcctNm;

	@JacksonXmlProperty(localName = "CorprtnNm")
	@Length(min = 1, max = 60)
	private String corprtnNm;

	@JacksonXmlProperty(localName = "CorprtnIDTp")
    //2020.6.15 应王喆需求,修改正则校验限制
    @Pattern(regexp = "IT[0-9]{2}")
	private String corprtnIDTp;

	@JacksonXmlProperty(localName = "CorprtnIDNo")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Length(min = 1, max = 64)
	private String corprtnIDNo;

	@JacksonXmlProperty(localName = "LglRepNm")
	@Length(min = 1, max = 60)
	private String lglRepNm;

	@JacksonXmlProperty(localName = "LglRepIDTp")
//	@Pattern(regexp = "IT01")
    //2020.6.4  应王喆需求,修改正则校验限制
	@Pattern(regexp = "IT[0-9]{2}")
	private String lglRepIDTp;

	@JacksonXmlProperty(localName = "LglRepIDNo")
	@Length(min = 1, max = 60)
	private String lglRepIDNo;

	@JacksonXmlProperty(localName = "Tel")
	@Length(min = 1, max = 35)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String tel;

	@JacksonXmlProperty(localName = "WltPtyId")
	@NotBlank
	@Length(min = 1, max = 14)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String wltPtyId;

	@JacksonXmlProperty(localName = "WltId")
	@NotBlank
	@Length(min = 1, max = 34)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String wltId;

	@JacksonXmlProperty(localName = "WltTp")
	@NotBlank
	@Pattern(regexp = "WT09")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String wltTp;

	@JacksonXmlProperty(localName = "WltLvl")
	@NotBlank
	@Pattern(regexp = "WL01")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String wltLvl;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if (this.getSgnAcctId() != null) {
			this.setSgnAcctId(encryptionHelper.encrypt(this.getSgnAcctId()));
		}
		if (this.getSgnAcctNm() != null) {
			this.setSgnAcctNm(encryptionHelper.encrypt(this.getSgnAcctNm()));
		}
		if (this.getLglRepNm() != null) {
			this.setLglRepNm(encryptionHelper.encrypt(this.getLglRepNm()));
		}
		if (this.getLglRepIDNo() != null) {
			this.setLglRepIDNo(encryptionHelper.encrypt(this.getLglRepIDNo()));
		}

		if (this.getTel() != null) {
			this.setTel(encryptionHelper.encrypt(this.getTel()));
		}

		// 4.10版文档新增了对这个字段的加解密
		if (this.getWltId() != null) {
			this.setWltId(encryptionHelper.encrypt(this.getWltId()));
		}

	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if (this.getSgnAcctId() != null) {
			this.setSgnAcctId(encryptionHelper.decrypt(this.getSgnAcctId()));
		}
		if (this.getSgnAcctNm() != null) {
			this.setSgnAcctNm(encryptionHelper.decrypt(this.getSgnAcctNm()));
		}
		if (this.getLglRepNm() != null) {
			this.setLglRepNm(encryptionHelper.decrypt(this.getLglRepNm()));
		}
		if (this.getLglRepIDNo() != null) {
			this.setLglRepIDNo(encryptionHelper.decrypt(this.getLglRepIDNo()));
		}

		if (this.getTel() != null) {
			this.setTel(encryptionHelper.decrypt(this.getTel()));
		}
		// 4.10版文档新增了对这个字段的加解密
		if (this.getWltId() != null) {
			this.setWltId(encryptionHelper.decrypt(this.getWltId()));
		}
	}

}
