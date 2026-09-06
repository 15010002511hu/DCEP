/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc381;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 交易信息
 * 
 * @author laimincai
 * @date 2023/12/29
 */
public class TrxInf implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 交易类型
     */
    @JacksonXmlProperty(localName = "TrxTp")
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "^[T]{2}[0-9]{2}")
    private String                  trxTp;

    /**
     * 交易金额
     */
    @JacksonXmlProperty(localName = "TrxAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount trxAmt;

    /**
     * 业务类型编码
     */
    @JacksonXmlProperty(localName = "TrxBizTp")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  trxBizTp;

    /**
     * 业务种类编码
     */
    @JacksonXmlProperty(localName = "TrxCtgyPurpCd")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 8)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  trxCtgyPurpCd;
    
    /**
     * 锁定失效日期
     */
    @JacksonXmlProperty(localName = "TmExp")
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
    private String tmExp;

    public String getTrxTp() {
        return trxTp;
    }

    public void setTrxTp(String trxTp) {
        this.trxTp = trxTp;
    }

    public ActiveCurrencyAndAmount getTrxAmt() {
        return trxAmt;
    }

    public void setTrxAmt(ActiveCurrencyAndAmount trxAmt) {
        this.trxAmt = trxAmt;
    }

    public String getTrxBizTp() {
        return trxBizTp;
    }

    public void setTrxBizTp(String trxBizTp) {
        this.trxBizTp = trxBizTp;
    }

    public String getTrxCtgyPurpCd() {
        return trxCtgyPurpCd;
    }

    public void setTrxCtgyPurpCd(String trxCtgyPurpCd) {
        this.trxCtgyPurpCd = trxCtgyPurpCd;
    }

    public String getTmExp() {
        return tmExp;
    }

    public void setTmExp(String tmExp) {
        this.tmExp = tmExp;
    }

    @Override
    public String toString() {
        return "TrxInf [trxTp=" + trxTp + ", trxAmt=" + trxAmt + ", trxBizTp=" + trxBizTp + ", trxCtgyPurpCd="
                + trxCtgyPurpCd + ", tmExp=" + tmExp + "]";
    }
}
