package com.dcep.dips.wholesalepayment.dto.dc211;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JacksonXmlRootElement(localName = "DbtrAgt")
@Setter
@Getter
@ToString
public class DbtrAgt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2053268255517482096L;

	@JacksonXmlProperty(localName = "FinInstnId")
	@NotNull
	@Valid
	private FinInstnId finInstnId;

}
