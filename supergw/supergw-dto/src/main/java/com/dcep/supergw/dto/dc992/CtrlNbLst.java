package com.dcep.supergw.dto.dc992;

import com.dcep.common.validator.Priority;
import com.dcep.common.validator.Priority.Highest;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;


@ToString
public class CtrlNbLst implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8283863983627759016L;

	@JacksonXmlProperty(localName = "CtrlNbRng")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 8)
	String ctrlNbRng;

	@JacksonXmlProperty(localName = "IDCInf")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 256)
	String iDCInf;

	public CtrlNbLst() {
	}

	public CtrlNbLst(@NotBlank(groups = Highest.class) @Length(min = 1, max = 8) String ctrlNbRng,
			@NotBlank(groups = Highest.class) @Length(min = 1, max = 256) String iDCInf) {
		super();
		this.ctrlNbRng = ctrlNbRng;
		this.iDCInf = iDCInf;
	}

	public String getCtrlNbRng() {
		return ctrlNbRng;
	}

	public void setCtrlNbRng(String ctrlNbRng) {
		this.ctrlNbRng = ctrlNbRng;
	}

	public String getiDCInf() {
		return iDCInf;
	}

	public void setiDCInf(String iDCInf) {
		this.iDCInf = iDCInf;
	}
}
