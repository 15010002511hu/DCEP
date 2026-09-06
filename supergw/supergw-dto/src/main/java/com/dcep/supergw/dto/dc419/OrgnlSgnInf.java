package com.dcep.supergw.dto.dc419;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OrgnlSgnInf implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4675327141004337352L;
	
	public OrgnlSgnInf() {}
	
	@JacksonXmlProperty(localName = "OrgnlMgmtTp")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@NotNull
	@Pattern(regexp = "MT03||MT05||MT06")
	private String orgnlMgmtTp;

	@JacksonXmlProperty(localName = "OrgnlSgnAcctTp")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String originalSignerAccountType;
}
