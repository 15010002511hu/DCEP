/*
 * pbcdci.cn Inc. Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc383;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 原报文主键组件
 * 
 * @author laimincai
 * @date 2023/12/29
 */
public class OrgnlGrpHdr implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 原报文标识号
     */
    @JacksonXmlProperty(localName = "OrgnlMsgId")
    @NotBlank
    @Length(min = 32, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String orgnlMsgId;

    /**
     * 原发起运营机构
     */
    @JacksonXmlProperty(localName = "OrgnlInstgPty")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 14, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String orgnlInstgPty;

    /**
     * 原锁定金额
     */
    @JacksonXmlProperty(localName = "OrgnlAmt")
    @Valid
    private ActiveCurrencyAndAmount orgnlAmt;

    public String getOrgnlMsgId() {
        return orgnlMsgId;
    }

    public void setOrgnlMsgId(String orgnlMsgId) {
        this.orgnlMsgId = orgnlMsgId;
    }

    public String getOrgnlInstgPty() {
        return orgnlInstgPty;
    }

    public void setOrgnlInstgPty(String orgnlInstgPty) {
        this.orgnlInstgPty = orgnlInstgPty;
    }

    public ActiveCurrencyAndAmount getOrgnlAmt() {
        return orgnlAmt;
    }

    public void setOrgnlAmt(ActiveCurrencyAndAmount orgnlAmt) {
        this.orgnlAmt = orgnlAmt;
    }

    @Override
    public String toString() {
        return "OrgnlGrpHdr [orgnlMsgId=" + orgnlMsgId + ", orgnlInstgPty=" + orgnlInstgPty
                + ", orgnlAmt=" + orgnlAmt + "]";
    }
}
