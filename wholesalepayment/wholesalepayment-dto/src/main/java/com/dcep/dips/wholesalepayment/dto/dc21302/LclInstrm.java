package com.dcep.dips.wholesalepayment.dto.dc21302;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@JacksonXmlRootElement(localName = "LclInstrm")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LclInstrm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5464073578307591519L;
	 /**
     * 业务类型编码
     */
    @JacksonXmlProperty(localName = "Prtry")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 4)
    private String                  prtry;

}
