package com.dcep.supergw.dto.dc536;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class OprlErr implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 6757674711940860770L;

	@JacksonXmlProperty(localName = "Err")
	@NotNull
	@Valid
	private Err err;

	/**
	 * 业务拒绝信息
	 */
	@JacksonXmlProperty(localName = "RjctInf")
	@Length(min = 1, max = 105)
	private String rjctInf;
}
