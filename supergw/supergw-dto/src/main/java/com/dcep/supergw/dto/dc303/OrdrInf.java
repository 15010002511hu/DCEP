package com.dcep.supergw.dto.dc303;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 3.6.1 统一下单请求报文<dcep.301.001.01>OutOrderInformation 订单信息
 * 
 * @author duzhong
 * @version $Id: TrxInf.java, v 0.1 2021年04月13日 上午10:18:43 duzhong Exp $
 *修改说明2021-09-26：商品名称字段长度由40改为200，by:weiqianjing
 */
@Getter
@Setter
@ToString
public class OrdrInf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1812395897836219538L;
	/**
	 * 受理订单生成时间
	 */
	@JacksonXmlProperty(localName = "OutOrdrTm")
	@NotBlank
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
	private String outOrdrTm;
	/**
	 * 受理订单号
	 */
	@JacksonXmlProperty(localName = "OutOrdrNo")
	@NotBlank
	@Length(min = 1, max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String outOrdrNo;

	/**
	 * 商户订单号
	 */
	@Length(min = 1, max = 64)
	@JacksonXmlProperty(localName = "MrchntOrdrNo")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String mrchntOrdrNo;

	/**
	 * 商品名称
	 */
	@JacksonXmlProperty(localName = "GdNm")
	@NotBlank
	@Length(min = 1, max = 200)
	private String gdNm;
	/**
	 * 订单详情
	 */
	@JacksonXmlProperty(localName = "OrdrDtls")
	@Length(min = 1, max = 4096)
	private String ordrDtls;
	
	/**
	 * 网络交易平台简称
	 */
	@JacksonXmlProperty(localName = "PltfrmNm")
	@Length(min = 1, max = 40)
	private String pltfrmNm;
	
	/**
	 * 交易地点
	 */
	@JacksonXmlProperty(localName = "TrxPlce")
	@NotBlank
	@Length(min = 1, max = 128)
	private String trxPlce;
	
	
	/**
	 * 订单失效时间
	 */
	@JacksonXmlProperty(localName = "OrdrTmExp")
	@NotBlank
	@Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
	private String ordrTmExp;
}
