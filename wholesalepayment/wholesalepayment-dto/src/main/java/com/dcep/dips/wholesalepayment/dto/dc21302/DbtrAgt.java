package com.dcep.dips.wholesalepayment.dto.dc21302;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JacksonXmlRootElement(localName = "DbtrAgt")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DbtrAgt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2053268255517482096L;

	@JacksonXmlProperty(localName = "FinInstnId")
	@NotNull
	@Valid
	private FinInstnId finInstnId;
	
	/**
     * 付款机构
     */
    @JacksonXmlProperty(localName = "BrnchId")
    @Valid
	@NotNull
    private BrnchId brnchId;

}
