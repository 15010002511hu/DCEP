package com.dcep.supergw.dto.dc721;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 离线转出通知信息
 *
 * @author duzhong
 * @version $Id: OfflineConvertNtfctnInf.java, v 0.1 2020年3月26日 上午11:32:47 duz
 *          Exp $
 */
@Getter
@Setter
@ToString
public class OfflineConvertNtfctnInf implements Serializable {

	private static final long serialVersionUID = -1235103987750779257L;

	/**
	 * 交易批次号
	 */
	@JacksonXmlProperty(localName = "BatchId")
	@Pattern(regexp = "^[B][0-9]{12}$")
	private String batchId;

	/**
	 * 明细业务总笔数
	 */
	@JacksonXmlProperty(localName = "NbOfTxs")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 15)
	private String nbOfTxs;

	/**
	 * 原始数字货币总金额
	 */
	@JacksonXmlProperty(localName = "TlOrigDCAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount tlOrigDCAmt;

	/**
	 * 余额凭证的余额总计
	 */
	@JacksonXmlProperty(localName = "TlTrxAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount tlTrxAmt;

	/**
	 * 余额凭证列表
	 */
	@JacksonXmlElementWrapper(localName = "BalVchrLst")
	@JacksonXmlProperty(localName = "BalVchrInf")
	@NotEmpty(groups = Priority.Highest.class)
	@Valid
	private List<BalVchrInf> balVchrLst;

}
