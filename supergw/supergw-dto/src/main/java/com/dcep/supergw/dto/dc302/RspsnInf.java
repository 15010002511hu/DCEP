package com.dcep.supergw.dto.dc302;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
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

@Getter
@Setter
@ToString
public class RspsnInf implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4479190568449432182L;

	/**
	 * 业务回执状态 不能为空，码值长度4
	 */
	@JacksonXmlProperty(localName = "RspsnSts")
	@NotBlank
	@Pattern(regexp = "PR01||PR02")
	@Length(min = 4, max = 4)
	private String rspsnSts;

	/**
	 * 业务拒绝码 标签可以不存在，存在必须有值
	 */
	@JacksonXmlProperty(localName = "RjctCd")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Length(min = 4, max = 4)
	@Pattern(regexp = "R[0-9]{3}")
	private String rjctCd;

	/**
	 * 业务拒绝信息
	 */
	@JacksonXmlProperty(localName = "RjctInf")
	@Length(min = 1, max = 105)
	private String rjctInf;

	/**
	 * 交易类型
	 */
	@JacksonXmlProperty(localName = "TrxTp")
	@NotBlank
	@Pattern(regexp = "TT[0-9]{2}")
	private String trxTp;

	/**
	 * 下单类型
	 * 禁止中文
	 */
	@JacksonXmlProperty(localName = "CreOrdrTp")
	@Pattern(regexp = "COT01||COT02||COT03")
	private String creOrdrTp;

	/**
	 * 订单金额
	 */
	@JacksonXmlProperty(localName = "TrxAmt")
	@NotNull(groups = Priority.Highest.class)
	private ActiveCurrencyAndAmount trxAmt;

	/**
	 * 商户号
	 */
	@JacksonXmlProperty(localName = "MrchntNo")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1,max = 35)
	private String mrchntNo;

	/**
	 * 商户订单号
	 */
	@JacksonXmlProperty(localName = "OutOrdrNo")
	@NotBlank
	@Length(min = 1, max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String outOrdrNo;

	/**
	 * 订单号生成时间
	 */
	@JacksonXmlProperty(localName = "OrdrTm")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
	private String ordrTm;

	/**
	 * 订单号
	 */
	@JacksonXmlProperty(localName = "OrdrNo")
	@Length(min = 1, max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String ordrNo;

	/**
	 * 附加数据
	 */
	@JacksonXmlProperty(localName = "Attach")
	@Length(min = 0, max = 128)
	@Valid
	private String attach;
	/**
	 * 推送子钱包信息
	 * 修改说明：新增推送子钱包信息域
	 */
	@Valid
	@JacksonXmlProperty(localName = "PushSubWltInf")
	private PushSubWltInf pushSubWltInf;

	/**
	 * 终端支付支持类型
	 */
	@Pattern(regexp = "BPOM00||BPOM01||BPOM02")
	@JacksonXmlProperty(localName = "BizPayMtd")
	private String bizPayMtd;

	/**
	 * 跳转信息
	 */
	@Valid
	@JacksonXmlProperty(localName = "RdrctInf")
	private RdrctInf rdrctInf;

}
