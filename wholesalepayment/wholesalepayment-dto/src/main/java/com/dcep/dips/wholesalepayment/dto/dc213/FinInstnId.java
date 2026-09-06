/**
 * 
 */
package com.dcep.dips.wholesalepayment.dto.dc213;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author sunxiaofeng
 *
 */
@JacksonXmlRootElement(localName = "FinInstnId")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FinInstnId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6014292637620438194L;

	@JacksonXmlProperty(localName = "ClrSysMmbId")
	@Valid
	@NotNull
	private ClrSysMmbId clrSysMmbId;
	
	/**
     * LEI编码
     */
    @JacksonXmlProperty(localName = "LEI")
    @NotBlank
    @Length(min = 1, max = 20)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            lei;

}
