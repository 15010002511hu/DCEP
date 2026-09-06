/**
 * 
 */
package com.dcep.dips.wholesalepayment.dto.dc211;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author sunxiaofeng
 *
 */
@JacksonXmlRootElement(localName = "Purp")
@Setter
@Getter
@ToString
public class Purp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4289951855422487374L;

	/**
	 * 业务种类编码
	 */
	@JacksonXmlProperty(localName = "Prtry")
	@NotBlank
	@Pattern(regexp = "^[0-9]{5,8}$", message = "业务种类编码仅允许5-8位数字")
	private String prtry;

}
