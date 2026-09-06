package com.dcep.supergw.dto.dc453;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 额度管理信息
 *
 * @author duzhong
 */
@Getter
@Setter
@ToString
public class ChckngInf {

	/**
	 * 对账日期
	 */
	@JacksonXmlProperty(localName = "ChckngDt")
	@NotBlank
	@Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd")
	private String chckngDt;

	/**
	 * 合作银行机构编码
	 */
	@JacksonXmlProperty(localName = "CoopBankInstnId")
	@NotBlank
	@Length(min = 1, max = 14)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String coopBankInstnId;

	/**
	 * 合作银行钱柜ID
	 */
	@JacksonXmlProperty(localName = "CoopBankWltId")
	@NotBlank
	@Length(min = 1, max = 34)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String coopBankWltId;

	/**
	 * 钱柜所属运营机构
	 */
	@JacksonXmlProperty(localName = "CshBoxInstnId")
	@NotBlank
	@Length(min = 1, max = 14)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String cshBoxInstnId;

	/**
	 * 期初余额
	 */
	@JacksonXmlProperty(localName = "InitlAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount initlAmt;

	/**
	 * 借贷标识
	 */
	@JacksonXmlProperty(localName = "CdtDbtInd")
	@NotBlank(groups = Priority.Highest.class)
	@Pattern(regexp = "CRDT||DBIT")
	private String cdtDbtInd;

	/**
	 * 借方金额
	 */
	@JacksonXmlProperty(localName = "DbtCntAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount dbtCntAmt;

	/**
	 * 贷方金额
	 */
	@JacksonXmlProperty(localName = "CdtCntAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount cdtCntAmt;

	/**
	 * 期末余额
	 */
	@JacksonXmlProperty(localName = "FnlAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount fnlAmt;

}
