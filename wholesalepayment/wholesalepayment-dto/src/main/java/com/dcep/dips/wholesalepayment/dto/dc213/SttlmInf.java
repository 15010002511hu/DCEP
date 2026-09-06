/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc213;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * 
 * @author sunxiaofeng
 * @version $Id: SttlmInf.java, v 0.1 2019年10月15日 上午10:17:26 sunxiaofeng Exp $
 */
@JacksonXmlRootElement(localName = "SttlmInf")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SttlmInf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 726242920617563277L;
	/**
	 * 结算方式
	 *
	 */
	@JacksonXmlProperty(localName = "SttlmMtd")
	@NotBlank
	@Length(min = 4, max = 4)
	@Pattern(regexp = "CLRG")
	private String sttlmMtd;
}
