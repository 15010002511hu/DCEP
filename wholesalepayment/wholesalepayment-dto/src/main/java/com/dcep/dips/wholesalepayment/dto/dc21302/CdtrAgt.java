/**
 * 
 */
package com.dcep.dips.wholesalepayment.dto.dc21302;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author sunxiaofeng
 *
 */
@JacksonXmlRootElement(localName = "CdtrAgt")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CdtrAgt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5423451217172525014L;

	@JacksonXmlProperty(localName = "FinInstnId")
	@NotNull
	@Valid
	private FinInstnId finInstnId;
	
	/**
     * 收款机构
     */
    @JacksonXmlProperty(localName = "BrnchId")
    @Valid
	@NotNull
    private BrnchId brnchId;

}
