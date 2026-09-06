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
public class RefInf implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1570781200189449723L;

	/**
	 * 退款金额
	 */
	@JacksonXmlProperty(localName = "TrxAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount trxAmt;
	
	/**
	 * 业务类型编码
	 */
	@JacksonXmlProperty(localName = "TrxBizTp")
	@NotBlank
	@Length(min = 1, max = 4)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Valid
	private String trxBizTp;
	
	/**
	 * 业务种类编码
	 */
	@JacksonXmlProperty(localName = "TrxCtgyPurpCd")
	@NotBlank
	@Length(min = 1, max = 8)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Valid
	private String trxCtgyPurpCd;
	
	/**
	 * 商户退款订单生成时间
	 */
	@JacksonXmlProperty(localName = "OutRefTm")
	@NotBlank
	@Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
	private String outRefTm;
	
	/**
	 * 商户退款订单号
	 */
	@JacksonXmlProperty(localName = "OutRefNo")
	@NotBlank
	@Length(min = 1, max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Valid
	private String outRefNo;
	
	/**
	 * 退款原因
	 */
	@JacksonXmlProperty(localName = "RefDesc")
	@Length(min = 1, max = 64)
	@Valid
	private String refDesc;
	   
	
}
