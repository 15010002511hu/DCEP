package com.dcep.supergw.dto.dc634;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author : duz
 * @version : RspsnInf.java v 0.1 2020-09-07
 * @description :634报文响应信息
 */
@Getter
@Setter
@ToString
public class RspsnInf {
	/**
	 * 业务回执状态 不能为空，码值长度4
	 */
	@JacksonXmlProperty(localName = "RspsnSts")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@NotBlank
	@Length(min = 4, max = 4)
	private String rspsnSts;

	/**
	 * 业务拒绝码 标签可以不存在，存在必须有值
	 */
	@JacksonXmlProperty(localName = "RjctCd")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Length(min = 4, max = 4)
	private String rjctCd;

	/**
	 * 业务拒绝信息
	 */
	@JacksonXmlProperty(localName = "RjctInf")
	@Length(min = 1, max = 105)
	private String rjctInf;

}
