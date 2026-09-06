package com.dcep.supergw.dto.dc437;

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
public class PtcInf  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4825869656755334966L;
	/**
     * 签约协议编号
     */
    @JacksonXmlProperty(localName = "PtcId")
    @NotBlank
    @Length(min = 1, max = 34)
    private String ptcId;

}
