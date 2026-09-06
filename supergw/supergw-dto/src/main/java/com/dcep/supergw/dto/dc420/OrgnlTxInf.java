package com.dcep.supergw.dto.dc420;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 原交易信息
 * 
 * @author duzhong
 */
@Getter
@Setter
@ToString
public class OrgnlTxInf implements Serializable, DataEncryption {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8556936580202139198L;
	@JacksonXmlProperty(localName = "MgmtTp")
	@NotBlank
	@Pattern(regexp = "MT03||MT05||MT06")
	private String mgmtTp;

	@JacksonXmlProperty(localName = "PtcId")
	@Length(min = 1, max = 34)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String ptcId;

	@JacksonXmlProperty(localName = "SgnChnl")
	@NotBlank
	@Pattern(regexp = "SC00||SC01||SC02")
	private String sgnChnl;

	@JacksonXmlProperty(localName = "SgnAcctPtyId")
	
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
	@Length(min = 1, max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String corprtnIDNo;

	@JacksonXmlProperty(localName = "LglRepNm")
	@Length(min = 1, max = 60)
	private String lglRepNm;

	@JacksonXmlProperty(localName = "LglRepIDTp")
    //2020.6.4  应王喆需求,修改正则校验限制
    @Pattern(regexp = "IT[0-9]{2}")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String lglRepIDTp;

	@JacksonXmlProperty(localName = "LglRepIDNo")
	@Length(min = 1, max = 60)
	private String lglRepIDNo;

	@JacksonXmlProperty(localName = "Tel")
	@Length(min = 1, max = 35)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String tel;

	@JacksonXmlProperty(localName = "SnglTxAmtLmt")
	@Valid
	private ActiveCurrencyAndAmount snglTxAmtLmt;

	@JacksonXmlProperty(localName = "DlTtlCnt")
	@Length(min = 1, max = 8)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String dlTtlCnt;

	@JacksonXmlProperty(localName = "DlTtlAmtLmt")
	@Valid
	private ActiveCurrencyAndAmount dlTtlAmtLmt;

	@JacksonXmlProperty(localName = "AnlTtlCnt")
	@Length(min = 1, max = 8)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String anlTtlCnt;

	@JacksonXmlProperty(localName = "AnlTtlAmtLmt")
	@Valid
	private ActiveCurrencyAndAmount anlTtlAmtLmt;

	@JacksonXmlProperty(localName = "PtcFctvDt")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2})?", message = "日期格式错误，正确格式是：yyyy-MM-dd")
	private String ptcFctvDt;

	@JacksonXmlProperty(localName = "PtcIfctvDt")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2})?", message = "日期格式错误，正确格式是：yyyy-MM-dd")
	private String ptcIfctvDt;

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
		
	}

}
