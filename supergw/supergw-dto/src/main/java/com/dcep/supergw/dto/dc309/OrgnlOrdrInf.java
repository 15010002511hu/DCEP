package com.dcep.supergw.dto.dc309;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class OrgnlOrdrInf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6074694458410735122L;


	/**
	 * 原商户号
	 */
	@JacksonXmlProperty(localName = "OrgnlMrchntNo")
	@NotBlank
	@Length(min = 1, max = 35)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Valid
	private String orgnlMrchntNo;


	/**
	 * 原商户订单号
	 */
	@JacksonXmlProperty(localName = "OrgnlOutOrdrNo")
	@NotBlank
	@Length(min = 1, max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Valid
	private String orgnlOutOrdrNo;
	
	/**
	 * 原订单号
	 */
	@JacksonXmlProperty(localName = "OrgnlOrdrNo")
	@NotBlank
	@Length(min = 1, max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Valid
	private String orgnlOrdrNo;
	
	/**
	 * 原订单金额
	 */
	@JacksonXmlProperty(localName = "OrgnlAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount orgnlAmt;
}
