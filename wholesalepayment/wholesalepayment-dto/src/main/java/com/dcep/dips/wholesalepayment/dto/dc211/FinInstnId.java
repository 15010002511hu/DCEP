/**
 * 
 */
package com.dcep.dips.wholesalepayment.dto.dc211;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author sunxiaofeng
 *
 */
@JacksonXmlRootElement(localName = "FinInstnId")
@Setter
@Getter
@ToString
public class FinInstnId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6014292637620438194L;

	@JacksonXmlProperty(localName = "ClrSysMmbId")
	@Valid
	@NotNull
	private ClrSysMmbId clrSysMmbId;

}
