package com.dcep.dips.wholesalepayment.dto.dc213;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author sunxiaofeng
 *
 */
@JacksonXmlRootElement(localName = "Tp")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Tp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2855576968591851399L;

	/**
	 * 付款人钱包类型/收款人钱包类型
	 */
	@JacksonXmlProperty(localName = "Prtry")
	@NotBlank
	@Pattern(regexp = "WT01||WT02||WT09||WT10")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String prtry;

}
