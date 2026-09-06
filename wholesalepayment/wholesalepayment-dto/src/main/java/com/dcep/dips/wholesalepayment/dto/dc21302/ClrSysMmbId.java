package com.dcep.dips.wholesalepayment.dto.dc21302;

import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckOrgState;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@JacksonXmlRootElement(localName = "ClrSysMmbId")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClrSysMmbId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5641530582119159516L;

	/**
	 * 付款运营机构/收款运营机构
	 */
	@JacksonXmlProperty(localName = "MmbId")
	@NotBlank
	@Length(min = 14, max = 14)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@CheckOrgState(groups = Priority.Lowest.class)
	private String mmbId;

}
