package com.dcep.supergw.dto.dc306;

import com.dcep.common.model.ActiveCurrencyAndAmount;
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
 * @author : maxinyu
 * @version : CpnInf.java v 0.1 2022-12-29
 * Copyright 2022 PBCDCI ALL Rights
 * @description :
 */
@ToString
@Getter
@Setter
public class CpnInf implements Serializable {
    private static final long serialVersionUID = -668259396935101992L;
    /**
     * 营销活动运营机构
     */
    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "PrmtGnrtdInstnId")
    String prmtGnrtdInstnId;

    /**
     * 营销活动编码
     */
    @NotBlank
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "PrmtId")
    String prmtId;

    /**
     * 营销活动名称
     */
    @NotBlank
    @Length(min = 1, max = 60)
    @JacksonXmlProperty(localName = "PrmtNm")
    String prmtNm;

    /**
     * 营销活动描述信息
     */
    @Length(min = 1, max = 256)
    @JacksonXmlProperty(localName = "PrmtDescInf")
    String prmtDescInf;

    /**
     * 营销活动类型
     */
    @NotBlank
    @Pattern(regexp = "PT01||PT02||PT03||PT04||PT05||PT06||PT07||PT99")
    @JacksonXmlProperty(localName = "PrmtTp")
    String prmtTp;

    /**
     * 权益ID
     */
    @NotBlank
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "CpnId")
    String cpnId;

    /**
     * 权益有效期
     */
    @NotBlank
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
    @JacksonXmlProperty(localName = "CpnExp")
    String cpnExp;

    /**
     * 权益金额
     */
    @NotNull
    @Valid
    @JacksonXmlProperty(localName = "CpnAmt")
    ActiveCurrencyAndAmount cpnAmt;

    /**
     * 权益退款属性
     */
    @NotBlank
    @Length(min = 1, max = 60)
    @Pattern(regexp = "CRP01||CRP02||CRP03||CRP04||CRP05")
    @JacksonXmlProperty(localName = "CpnRefPrprty")
    String cpnRefPrprty;

    /**
     * 权益描述信息
     */
    @Length(min = 1, max = 256)
    @JacksonXmlProperty(localName = "CpnDescInf")
    String cpnDescInf;

    /**
     * 备注信息
     */
    @Length(min = 1, max = 128)
    @JacksonXmlProperty(localName = "RmkInf")
    String rmkInf;


}
