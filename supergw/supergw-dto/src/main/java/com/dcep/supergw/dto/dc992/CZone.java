package com.dcep.supergw.dto.dc992;

import com.dcep.common.validator.Priority;
import com.dcep.common.validator.Priority.Highest;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@ToString
public class CZone implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5033693070102556824L;
	@JacksonXmlProperty(localName = "IDCInf")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 256)
	String iDCInf;

	public CZone(@NotBlank(groups = Highest.class) @Length(min = 1, max = 256) String iDCInf) {
		super();
		this.iDCInf = iDCInf;
	}

	public String getiDCInf() {
		return iDCInf;
	}

	public void setiDCInf(String iDCInf) {
		this.iDCInf = iDCInf;
	}
	
}
