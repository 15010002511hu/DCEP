package com.dcep.supergw.dto.dc310;

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
public class RspsnInf implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3380759080712708814L;

	/**
	 * 业务回执状态 不能为空，码值长度4
	 */
	@JacksonXmlProperty(localName = "RspsnSts")
	@NotBlank
	@Pattern(regexp = "PR01||PR02")
	@Length(min = 4, max = 4)
	private String rspsnSts;

	/**
	 * 业务拒绝码 标签可以不存在，存在必须有值
	 */
	@JacksonXmlProperty(localName = "RjctCd")
	@Length(min = 4, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "R[0-9]{3}")
	private String rjctCd;

	/**
	 * 业务拒绝信息
	 */
	@JacksonXmlProperty(localName = "RjctInf")
	@Length(min = 1, max = 105)
	private String rjctInf;

	/**
	 * 商户退款单号
	 */
	@JacksonXmlProperty(localName = "OutRefNo")
	@NotBlank
	@Length(min = 1,max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String outRefNo;
	
	/**
	 * 退款金额
	 */
	@JacksonXmlProperty(localName = "TrxAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount trxAmt;
	
	/**
	 * 商户退款订单生成时间
	 */
	@JacksonXmlProperty(localName = "RefTm")
	@Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
	private String refTm;

	/**
	 * 商户退款订单号
	 */
	@JacksonXmlProperty(localName = "RefNo")
	@Length(min = 1, max = 64)
	@Valid
	private String refNo;
}
