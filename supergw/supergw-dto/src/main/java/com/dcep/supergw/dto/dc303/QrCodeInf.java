package com.dcep.supergw.dto.dc303;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 收款码信息
 * 
 * @author duzhong
 */
@Getter
@Setter
@ToString
public class QrCodeInf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1305315896198363493L;
	/**
	 * 收款码
	 */
	@JacksonXmlProperty(localName = "QrCode")
	@NotBlank
	@Length(min = 1, max = 34)
	private String qrCode;
}
