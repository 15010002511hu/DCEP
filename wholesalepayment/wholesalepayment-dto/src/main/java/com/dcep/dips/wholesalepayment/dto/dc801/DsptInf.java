package com.dcep.dips.wholesalepayment.dto.dc801;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
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
 * 差错信息
 * 
 * @author chenxf
 * @version $Id: TrxInf.java, v 0.1 2019年12月3日 下午4:26:30 chenxf Exp $
 */
@JacksonXmlRootElement(localName = "DsptInf")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DsptInf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 差错业务类型编码(规定只能为E100)
	 */
	@JacksonXmlProperty(localName = "DsptBizTp")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 3, max = 4)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String dsptBizTp;

	/**
	 * 差错业务种类编码(规定只能为08001或者08002)
	 */
	@JacksonXmlProperty(localName = "DsptCtgyPurpCd")
	@NotBlank(groups = Priority.Highest.class)
	@Pattern(regexp = "^[0-9]{5,8}$", message = "业务种类编码仅允许5-8位数字")
	private String dsptCtgyPurpCd;

	/**
	 * 差错原因码
	 */
	@JacksonXmlProperty(localName = "DsptRsnCd")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 4, max = 4)
	@Pattern(regexp = "^[D][R][0-9]{2}")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String dsptRsnCd;
	
	/**
	 * 差错原因说明
	 */
	@JacksonXmlProperty(localName = "DsptRsnDesc")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 0, max = 64)
	private String dsptRsnDesc;
	
	/**
	 * 调账金额
	 */
	@JacksonXmlProperty(localName = "DsptAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount dsptAmt;

	/**
	 * 交易批次号
	 */
	@JacksonXmlProperty(localName = "BatchId")
	@Pattern(regexp = "^[B][0-9]{12}$")
	@NotBlank(groups = Priority.Highest.class)
	private String batchId;

	/**
	 * SysWorkDt 系统工作日期
	 */
	@JacksonXmlProperty(localName = "SysWorkDt")
	@NotBlank
	@Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd")
	private String                  sysWorkDt;

	/**
	 * SttlmDt 结算日期
	 */
	@JacksonXmlProperty(localName = "SttlmDt")
	@Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd")
	private String                  sttlmDt;
	
	/**
	 * 原交易信息
	 */
	@JacksonXmlProperty(localName = "OrgnlTxRef")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private OrgnlTxRef orgnlTxRef;

}
