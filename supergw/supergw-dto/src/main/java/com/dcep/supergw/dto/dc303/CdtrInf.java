package com.dcep.supergw.dto.dc303;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 
 * 3.6.1 统一下单请求报文<dcep.301.001.01>CreditorInformation 收款运营机构信息
 * 
 * @author duzhong
 * @version $Id: TrxInf.java, v 0.1 2021年04月13日 上午10:18:43 duzhong Exp $
 *
 */

@Getter
@Setter
@ToString
public class CdtrInf implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3984755574746880636L;
	/**
	 * 商户所属运营机构
	 */
	@JacksonXmlProperty(localName = "CdtrInstnId")
	@NotBlank
	@Length(min = 1, max = 14)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String cdtrInstnId;
	/**
	 * 商户钱包ID
	 */
	@JacksonXmlProperty(localName = "MrchntWltId")
	@Length(min = 1, max = 34)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String mrchntWltId;

	/**
	 * 商户属性
	 */
	@Pattern(regexp = "MP[0-9]{2}")
	@JacksonXmlProperty(localName = "MrchntPrprty")
	private String mrchntPrprty;

	/**
	 * 商户编码
	 */
	@JacksonXmlProperty(localName = "MrchntNo")
	@NotBlank
	@Length(min = 1, max = 35)
	private String mrchntNo;
	/**
	 * 商户名称
	 */
	@JacksonXmlProperty(localName = "MrchntNm")
	@NotBlank
	@Length(min = 1, max = 60)
	private String mrchntNm;
	/**
	 * 商户简称
	 */
	@JacksonXmlProperty(localName = "MrchntAbbrNm")
	@Length(min = 1, max = 30)
	@NotBlank
	private String mrchntAbbrNm;
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
	@Length(min = 1, max = 4)
	@Pattern(regexp = "IT[0-9]{2}")
	private String mrchntIdTp;

	/**
	 * 商户证件编码
	 */
	@JacksonXmlProperty(localName = "MrchntIdNo")
	@Length(min = 1, max = 32)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String mrchntIdNo;

	/**
	 * 受理终端编号
	 */
	@JacksonXmlProperty(localName = "TerNo")
	@Length(min = 1, max = 32)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@NotBlank
	private String terNo;

	/**
	 * 受理终端IP
	 */
	@JacksonXmlProperty(localName = "TerIP")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Length(min = 1, max = 64)
	@NotBlank
	private String terIP;

}
