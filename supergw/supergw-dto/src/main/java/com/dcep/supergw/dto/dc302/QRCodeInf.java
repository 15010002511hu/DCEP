package com.dcep.supergw.dto.dc302;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
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
public class QRCodeInf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1305315896198363493L;
	/**
	 * 收款码
	 */
	@JacksonXmlProperty(localName = "QrCode")
	@Length(min = 1, max = 500)
	private String qrCode;
}
