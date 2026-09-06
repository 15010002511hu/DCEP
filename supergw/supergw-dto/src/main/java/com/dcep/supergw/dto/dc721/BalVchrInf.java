package com.dcep.supergw.dto.dc721;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 余额凭证对象
 *
 * @author chenkai
 * @version $Id: BalVchr.java, v 0.1 2020年3月26日 上午11:32:47 chenkai Exp $
 */
@Getter
@Setter
@ToString
public class BalVchrInf implements Serializable {

	private static final long serialVersionUID = -1286526795617680116L;

	/**
	 * 序号
	 */
	@JacksonXmlProperty(localName = "BalVchr")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 2048)
	private String balVchr;

	/**
	 * 交易流水列表
	 */

	@JacksonXmlElementWrapper(localName = "TranBillLst")
	@JacksonXmlProperty(localName = "TranBill")
	@NotEmpty(groups = Priority.Highest.class)
	@Valid
	private List<TranBill> tranBllLst;
}
