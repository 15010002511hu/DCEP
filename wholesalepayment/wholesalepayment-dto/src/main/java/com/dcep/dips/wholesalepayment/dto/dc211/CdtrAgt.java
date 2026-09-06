/**
 * 
 */
package com.dcep.dips.wholesalepayment.dto.dc211;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author sunxiaofeng
 *
 */
@JacksonXmlRootElement(localName = "CdtrAgt")
@Setter
@Getter
@ToString
public class CdtrAgt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5423451217172525014L;

	@JacksonXmlProperty(localName = "FinInstnId")
	@NotNull
	@Valid
	private FinInstnId finInstnId;

}
