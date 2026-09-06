package com.dcep.supergw.dto.dc507;

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
 * @author duzhong
 */
@JacksonXmlRootElement(localName = "MrchntInf")
@Getter
@Setter
@ToString
public class MrchntInf implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8648347096541667819L;
	/**
     	*商户号
     */
	@JacksonXmlProperty(localName = "MrchntNo")
    @Length(min = 1,max = 35)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@NotBlank
	private String mrchntNo;
    
	
}
