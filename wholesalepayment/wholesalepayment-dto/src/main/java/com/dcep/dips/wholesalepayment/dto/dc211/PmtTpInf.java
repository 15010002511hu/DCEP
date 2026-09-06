package com.dcep.dips.wholesalepayment.dto.dc211;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JacksonXmlRootElement(localName = "PmtTpInf")
@Setter
@Getter
@ToString
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
