package com.dcep.supergw.dto.dc991;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
@Getter
@Setter
@ToString
public class ProbeInf implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5754450245112951130L;
	/**
	 * 明细业务总笔数
	 */
	@JacksonXmlProperty(localName = "InstgId")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 14)
	private String instgId;
}
