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
 * ProtocolInformation 协议信息
 * 
 * @author duz
 */
@JacksonXmlRootElement(localName = "PtcInf")
@Getter
@Setter
@ToString
public class PtcInf implements Serializable {

	private static final long serialVersionUID = -2146890622546168422L;

	/**
	 * 签约协议号
	 */
	@JacksonXmlProperty(localName = "PtcId")
	@NotBlank
	@Length(min = 1, max = 34)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String ptcId;

	/**
	 * 钱包ID辩识码
	 */
	@JacksonXmlProperty(localName = "WltShrtId")
	@NotBlank
	@Length(min = 4, max = 4)
	private String wltShrtId;

	

}
