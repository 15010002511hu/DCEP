/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc261;

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

	private static final long serialVersionUID = 5473004578694791485L;

	/**
	 * 交易类型 TT00：普通汇款 TT01：扫码支付 TT02：碰一碰支付 TT03：统一下单支付 TT04：H5拉起支付
	 */
	@JacksonXmlProperty(localName = "TrxTp")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 4, max = 4)
	@Pattern(regexp = "^[T]{2}[0-9]{2}")
	private String trxTp;

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
	 * 交易金额
	 */
	@JacksonXmlProperty(localName = "TrxAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount trxAmt;

	/**
	 * 二维码类型 QT01：个人收款码 QT02：个人付款码 QT03：商户静态码 QT04：商户动态码
	 */
	@JacksonXmlProperty(localName = "QrCodeTp")
	@Length(min = 4, max = 4)
	@Pattern(regexp = "^[Q][T][0-9]{2}")
	private String qrCodeTp;
	
	/**
     * 二维码
     */
    @JacksonXmlProperty(localName = "QrCode")
    @Length(min = 1, max = 1024)
    private String qrCode;
    
    /**
     * 收款码编码类型
     * QET01：数字人民币收款码 QET02：银联标准收款码 QET03：聚合收款码 QET04：微信收款码 QET05：支付宝收款码
     */
    @JacksonXmlProperty(localName = "QrEncdgTp")
    @Length(min = 5, max = 5)
    @Pattern(regexp = "^[Q][E][T][0-9]{2}")
    private String qrEncdgTp;

}
