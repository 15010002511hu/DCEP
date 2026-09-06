package com.dcep.dips.wholesalepayment.dto.dc261;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 订单信息
 * 
 * @author chenxf
 *
 */
@JacksonXmlRootElement(localName = "OrderInf")
@Setter
@Getter
@ToString
public class OrderInf implements Serializable {

	private static final long serialVersionUID = 1754171405056340692L;

	/**
	 * 订单号
	 */
	@JacksonXmlProperty(localName = "OrdrNo")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 64)
	private String ordrNo;

	/**
	 * 订单时间
	 */
	@JacksonXmlProperty(localName = "OrdrTm")
	@NotBlank(groups = Priority.Highest.class)
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
	private String ordrTm;

	/**
	 * 商品名称
	 */
	@JacksonXmlProperty(localName = "GdNm")
	@Length(min = 1, max = 200)
	private String gdNm;

	/**
	 * 订单详情
	 */
	@JacksonXmlProperty(localName = "OrdrDtls")
	@Length(min = 1, max = 4096)
	private String ordrDtls;

}
