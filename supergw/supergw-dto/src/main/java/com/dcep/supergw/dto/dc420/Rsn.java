package com.dcep.supergw.dto.dc420;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
/**
 * 原交易原因
 * @author duzhong
 */
@Getter
@Setter
@ToString
public class Rsn implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1099713790927915844L;

	@JacksonXmlProperty(localName = "RjctCd")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Length(min = 1, max = 4)
	private String rjctCd;
	
	@JacksonXmlProperty(localName = "RjctInf")
	@Length(min = 1, max = 105)
	private String rjctInf;

}
