package com.dcep.dips.wholesalepayment.dto.dc21302;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JacksonXmlRootElement(localName = "PmtTpInf")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PmtTpInf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5298926752624052980L;
	@JacksonXmlProperty(localName = "LclInstrm")
	@NotNull
	@Valid
	private LclInstrm lclInstrm;

}
