package com.dcep.dips.wholesalepayment.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 钱柜机构信息
 */

@JacksonXmlRootElement(localName = "CshBoxInf")
@Getter
@Setter
@ToString
public class CshBoxInf implements Serializable {

	private static final long serialVersionUID = -3296309420151379296L;

	/**
	 * 合作银行机构编码
	 */
	@JacksonXmlProperty(localName = "CoopBankInstnId")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 14, max = 14)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String coopBankInstnId;

	/**
	 * 合作银行钱柜ID
	 */
	@JacksonXmlProperty(localName = "CoopBankWltId")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 16, max = 16)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String coopBankWltId;

	/**
	 * 钱柜所属运营机构
	 */
	@JacksonXmlProperty(localName = "CshBoxInstnId")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 14, max = 14)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String cshBoxInstnId;

	public CshBoxInf() {
	}
	
	public CshBoxInf(String coopBankInstnId, String coopBankWltId, String cshBoxInstnId) {
		this.coopBankInstnId = coopBankInstnId;
		this.coopBankWltId = coopBankWltId;
		this.cshBoxInstnId = cshBoxInstnId;
	}

}
