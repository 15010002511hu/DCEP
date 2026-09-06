/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc227;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author lizhiguo
 * @version $Id: CdtrInf.java, v 0.1 20200427 lizhiguo Exp $
 */

@JacksonXmlRootElement(localName = "TrxInf")
@Getter
@Setter
@ToString
public class TrxInf implements Serializable {
	
	private static final long serialVersionUID = -7286780355835048223L;

	/**
     * 业务类型代码
     */
    @JacksonXmlProperty(localName = "TrxBizTp")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 3, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  trxBizTp;

    /**
     * 业务种类代码
     */
    @JacksonXmlProperty(localName = "TrxCtgyPurpCd")
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "^[0-9]{5,8}$", message = "业务种类编码仅允许5-8位数字")
    private String                  trxCtgyPurpCd;

    /**
     * 交易金额
     */
    @JacksonXmlProperty(localName = "TrxAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount trxAmt;

    /**
     * 交易用途
     */
	/*
	 * @JacksonXmlProperty(localName = "TrxPrps")
	 * 
	 * @Length(min = 4, max = 4)
	 * 
	 * @Pattern(regexp = "TP00||TP01||TP02||TP03||TP04||TP05||TP06||TP99") private
	 * String trxPrps;
	 */
    
    /**
     * 交易批次号
     */
    @JacksonXmlProperty(localName = "BatchId")
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "^[B][0-9]{12}$")
    private String    batchId;
    
    /*
     * 充值流水号
     */
    @JacksonXmlProperty(localName = "TopupSrlNo")
    @Length(min = 1, max = 64) 
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String topupSrlNo;

    /*
     * Unstructured附言/Postscript/Value
     */
    @JacksonXmlProperty(localName = "Postscript")
    @Length(min = 1, max = 120) 
    private String     postscript;

    /**
     * ChanlTp 渠道类型
     */
    @JacksonXmlProperty(localName = "ChanlTp")
    @NotBlank
    @Pattern(regexp = "^CT[0-9]{2}$")
    private String                  chanlTp;

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
}
