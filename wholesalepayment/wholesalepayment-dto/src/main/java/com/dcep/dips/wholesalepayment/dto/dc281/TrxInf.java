/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc281;

import com.dcep.common.model.ActiveCurrencyAndAmount;
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
 * @author gcg
 * @version $Id: TrxInf.java, v 0.1 2019年8月27日 上午11:16:59 gcg Exp $
 */

@JacksonXmlRootElement(localName = "TrxInf")
@Getter
@Setter
@ToString
public class TrxInf implements Serializable {

    /**  */
    private static final long       serialVersionUID = 5473004578694791485L;

    /**
     * 业务类型代码
     */
    @JacksonXmlProperty(localName = "TrxBizTp")
    @NotBlank
    @Length(min = 3, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  trxBizTp;

    /**
     * 业务种类代码
     */
    @JacksonXmlProperty(localName = "TrxCtgyPurpCd")
    @NotBlank
    @Pattern(regexp = "^[0-9]{5,8}$", message = "业务种类编码仅允许5-8位数字")
    private String                  trxCtgyPurpCd;

    /**
     * 交易金额
     */
    @JacksonXmlProperty(localName = "TrxAmt")
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount trxAmt;

    /**
     * 交易批次号
     */
    @JacksonXmlProperty(localName = "BatchId")
    @Pattern(regexp = "^[B][0-9]{12}$")
    @NotBlank
    private String                  batchId;

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
