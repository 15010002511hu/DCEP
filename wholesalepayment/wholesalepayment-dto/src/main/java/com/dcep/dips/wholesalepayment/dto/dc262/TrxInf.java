/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc262;

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
 * @author liuqi
 * @version $Id: TrxInf.java, v 0.1 2019年8月27日 下午4:43:58 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "TrxInf")
@Getter
@Setter
@ToString
public class TrxInf implements Serializable {

    /**  */
    private static final long       serialVersionUID = 5473004578694791485L;
    /**
     * 交易批次号
     */
    @JacksonXmlProperty(localName = "BatchId")
    @Pattern(regexp = "^[B][0-9]{12}$")
    private String                  batchId;
    
    /**
	 * 交易类型 TT00：普通汇款 TT01：扫码支付 TT02：碰一碰支付 TT03：统一下单支付 TT04：H5拉起支付
	 */
	@JacksonXmlProperty(localName = "TrxTp")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 4, max = 4)
	@Pattern(regexp = "^[T]{2}[0-9]{2}")
	private String trxTp;

    /**
     * 交易金额
     */
    @JacksonXmlProperty(localName = "TrxAmt")
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount trxAmt;

    /**
	 * 业务类型代码
	 */
	@JacksonXmlProperty(localName = "TrxBizTp")
	@Length(min = 3, max = 4)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String trxBizTp;

	/**
	 * 业务种类编码
	 */
	@JacksonXmlProperty(localName = "TrxCtgyCd")
	@Pattern(regexp = "^[0-9]{5,8}$", message = "业务种类编码仅允许5-8位数字")
	private String trxCtgyCd;

	/**
	 * 交易备注
	 */
	@JacksonXmlProperty(localName = "TrxRmk")
	@Length(min = 1, max = 32)
	private String trxRmk;

	/**
	 * SysWorkDt 系统工作日期
	 */
	@JacksonXmlProperty(localName = "SysWorkDt")
	@NotBlank
	@Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd")
	private String                  sysWorkDt;
}