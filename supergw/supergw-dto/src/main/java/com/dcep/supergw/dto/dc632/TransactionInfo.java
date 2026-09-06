package com.dcep.supergw.dto.dc632;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class TransactionInfo {

	@JacksonXmlProperty(localName = "TransactionIndex")
	@NotBlank
	@Length(min = 1, max = 32)
	private String transactionIndex;

	@JacksonXmlProperty(localName = "TransactionStatus")
	@Length(min = 1, max = 2)
	@Pattern(regexp = "00||01||02")
	@NotBlank
	private String transactionStatus;
}
