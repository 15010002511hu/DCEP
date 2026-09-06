package com.dcep.dips.wholesalepayment.dto.dc281;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 受理服务机构信息
 * 
 * @author chenxingfeng
 *
 */
@JacksonXmlRootElement(localName = "AcqAgtInf")
@Setter
@Getter
@ToString
public class AcqAgtInf implements Serializable {

	private static final long serialVersionUID = -2915320423402667995L;

	/**
	 * 受理机构的金融机构编码
	 */
	@JacksonXmlProperty(localName = "AcqAgtInstnId")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 14)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String acqAgtInstnId;

	/**
	 * 受理机构的机构名称
	 */
	@JacksonXmlProperty(localName = "AcqAgtNm")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 60)
	private String acqAgtNm;

}
