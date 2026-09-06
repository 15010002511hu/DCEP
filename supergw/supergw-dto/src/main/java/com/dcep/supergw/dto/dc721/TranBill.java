package com.dcep.supergw.dto.dc721;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 交易流水信息
 *
 * @author duzhong
 * @version $Id: OfflineConvertNtfctnInf.java, v 0.1 2020年3月26日 上午11:32:47 duz
 *          Exp $
 */
public class TranBill {


    /**
     * 交易类型
     */
	@JacksonXmlProperty(localName = "TranType")
	@NotBlank(groups = Priority.Highest.class)
	@Pattern(regexp = "0001||0002||0003||0004")
	private String tranType;
	
    /**
     * 交易业务类型
     */
	@JacksonXmlProperty(localName = "TranBizType")
	@Length(min = 1,max = 4)
	@NotBlank(groups = Priority.Highest.class)
	private String tranBizType;
	
    /**
     * 业务种类
     */
	@JacksonXmlProperty(localName = "TranCtgPur")
	@Length(min = 1,max = 5)
	@NotBlank(groups = Priority.Highest.class)
	private String tranCtgPur;
	
    /**
     * 交易金额
     */
	@JacksonXmlProperty(localName = "Amount")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount amount;
	
    /**
     * 交易时间
     */
	@JacksonXmlProperty(localName = "TranTms")
	@NotBlank
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
	private String TranTms;
	
    /**
     * 交易索引
     */
	@JacksonXmlProperty(localName = "TranIndx")
	@Length(min = 1,max = 32)
	@NotNull
	private String tranIndx;
	
    /**
     * 付款钱包ID
     */
	@JacksonXmlProperty(localName = "DebtorWalletId")
	@NotNull
	@Length(min = 1,max = 34)
	private String debtorWalletId;
	
    /**
     * 收款钱包ID
     */
	@JacksonXmlProperty(localName = "CreditorWalletId")
	@NotNull
	@Length(min = 1,max = 34)
	private String creditorWalletId;
	
    /**
     * 备注
     */
	@JacksonXmlProperty(localName = "Remark")
	@Length(min = 1,max = 240)
	private String remark;
	
}
