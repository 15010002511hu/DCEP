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
 */
@JacksonXmlRootElement(localName = "MrchntInf")
@Getter
@Setter
@ToString
public class MrchntInf2 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5736221282856132674L;

	/**
	 * 商户证件类型
	 */
	@JacksonXmlProperty(localName = "MrchntIdTp")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Pattern(regexp = "IT[0-9]{2}")
	@NotBlank
	private String mrchntIdTp;

	/**
	 * 商户证件编号
	 */
	@JacksonXmlProperty(localName = "MrchntIdNo")
	@Length(min = 1, max = 32)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@NotBlank
	private String mrchntIdNo;

	/**
	 * 场景ID
	 */
	@JacksonXmlProperty(localName = "SceneId")
	@Length(min = 1, max = 10)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@NotBlank
	private String sceneId;

}
