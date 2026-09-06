package com.dcep.supergw.dto.dc507;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author laimincai
 * @version : MrchntPtcInf.java v 0.1 2023-01-05
 * @description : 用户签署商户协议信息
 */
@Getter
@Setter
@ToString
public class MrchntPtcInf implements Serializable {

	private static final long serialVersionUID = -5429611846364788811L;

	/**
	 * 协议版本号
	 */
	@JacksonXmlProperty(localName = "PtcVer")
	@NotBlank
	@Length(min = 1, max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String ptcVer;
	
	/**
	 * 协议名称
	 */
	@JacksonXmlProperty(localName = "PtcNm")
	@NotBlank
	@Length(min = 1, max = 64)
	private String ptcNm;
}
