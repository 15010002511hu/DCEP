package com.dcep.supergw.dto.dc303;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 
 * 3.6.1 统一下单请求报文<dcep.301.001.01>AcquiringAgentInformation 受理方机构信息
 * 
 * @author duzhong
 * @version $Id: TrxInf.java, v 0.1 2021年04月13日 上午10:18:43 duzhong Exp $
 *
 */

@Getter
@Setter
@ToString
public class AcqAgtInf implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3577662539787624579L;
	/**
	 * 受理机构金融编码
	 */
	@JacksonXmlProperty(localName = "AcqAgtInstnId")
	@NotBlank
	@Length(min = 1, max = 14)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String acqAgtInstnId;
	/**
	 * 受理机构名称
	 */
	@JacksonXmlProperty(localName = "AcqAgtNm")
	@NotBlank
	@Length(min = 1, max = 60)
	private String acqAgtNm;

}
