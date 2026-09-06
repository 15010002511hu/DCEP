package com.dcep.supergw.dto.dc722;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 离线转出核对确认信息
 *
 * @author duzhong
 * @version $Id: OfflineConvertCnfrmInf.java, v 0.1 2020年3月26日 上午11:32:47 duz
 *          Exp $
 */
@Getter
@Setter
@ToString
public class OfflineConvertCnfrmInf implements Serializable {

	private static final long serialVersionUID = -7677743239294458309L;
	/**
	 * 业务回执状态
	 */
	@JacksonXmlProperty(localName = "RspsnSts")
	@NotBlank(groups = Priority.Highest.class)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String rspsnSts;
	/**
	 * 业务拒绝码
	 */
	@JacksonXmlProperty(localName = "RjctCd")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String rjctCd;
	/**
	 * 业务拒绝信息
	 */

	@JacksonXmlProperty(localName = "RjctInf")
	@Length(min = 1, max = 105)
	private String rjctInf;

	/**
	 * 请求交易业务总笔数
	 */
	@JacksonXmlProperty(localName = "ReqNbOfTxs")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 15)
	private String reqNbOfTxs;

	/**
	 * 余额凭证的余额总计
	 */
	@JacksonXmlProperty(localName = "TlTrxAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount tlTrxAmt;

	/**
	 * 交易批次号
	 */
	@JacksonXmlProperty(localName = "BatchId")
	@Pattern(regexp = "^[B][0-9]{12}$")
	private String batchId;

}
