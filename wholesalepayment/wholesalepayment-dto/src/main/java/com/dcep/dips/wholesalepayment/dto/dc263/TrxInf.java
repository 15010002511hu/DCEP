/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc263;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author chenxf
 * @version $Id: TrxInf.java
 */
@JacksonXmlRootElement(localName = "TrxInf")
@Getter
@Setter
@ToString
public class TrxInf implements Serializable {
	/**  */
	private static final long serialVersionUID = -6770447433467667071L;

	/**
	 * 交易批次号
	 */
	@JacksonXmlProperty(localName = "BatchId")
	@Pattern(regexp = "^[B][0-9]{12}$")
	private String batchId;

	/**
	 * 交易金额
	 */
	@JacksonXmlProperty(localName = "TrxAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount trxAmt;

	/**
	 * 订单号
	 */
	@JacksonXmlProperty(localName = "OrdrNo")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 64)
	private String ordrNo;

	/**
	 * ChanlTp 渠道类型
	 */
	@JacksonXmlProperty(localName = "ChanlTp")
	@NotBlank
	@Pattern(regexp = "^CT[0-9]{2}$")
	private String                  chanlTp;

	/**
	 * SysWorkDt 系统工作日期
	 */
	@JacksonXmlProperty(localName = "SysWorkDt")
	@NotBlank
	@Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd")
	private String                  sysWorkDt;

	/**
	 * SttlmDt 结算日期
	 */
	@JacksonXmlProperty(localName = "SttlmDt")
	@Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd")
	private String                  sttlmDt;
}
