package com.dcep.supergw.dto.dc509;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 商户信息
 * 
 * @author duzhong
 * 修改说明：2021-10-11：修改MrchntNo的字段长度为35
 */
@JacksonXmlRootElement(localName = "MrchntInf")
@Getter
@Setter
@ToString
public class MrchntInf implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 347055134841616435L;

	/**
	 * 商户证件编号
	 */
	@JacksonXmlProperty(localName = "MrchntNo")
	@Length(min = 1, max = 35)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@NotBlank
	private String mrchntNo;
}
