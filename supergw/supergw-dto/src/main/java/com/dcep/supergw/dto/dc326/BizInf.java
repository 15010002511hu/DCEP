package com.dcep.supergw.dto.dc326;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@ToString
public class BizInf implements Serializable {
    private static final long serialVersionUID = -6869707625576747377L;
    /*
     * 开通标识
     */
    @JacksonXmlProperty(localName = "OpenFlg")
    @NotBlank
    @Length(min = 1, max = 4)
    @Pattern(regexp = "OF[0-9]{2}||OA00||OA01")
    private String openFlg;

    /*
     * 钱包等级
     */
    @JacksonXmlProperty(localName = "WltLvl")
    @Length(min = 4, max = 4)
    @Pattern(regexp = "WL01||WL02||WL03||WL04||WL05")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String wltLvl;

    /*
     * 是否为境外钱包
     */
    @JacksonXmlProperty(localName = "IsOvrSeaWlt")
    @Pattern(regexp = "true||false")
    private String isOvrSeaWlt;

    /*
     * 钱包ID辨识码
     */
    @JacksonXmlProperty(localName = "WltShrtId")
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String wltShrtId;

    /**
     * 钱包ID
     */
    @JacksonXmlProperty(localName = "WltId")
    @Length(min = 16, max = 16)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String wltId;

    /*
     * 单笔交易金额上限
     */
    @JacksonXmlProperty(localName = "WltSnglTxAmtLmt")
    @Valid
    private ActiveCurrencyAndAmount wltSnglTxAmtLmt;

    /*
     * 当日交易金额上限
     */
    @JacksonXmlProperty(localName = "WltDayTtlAmtLmt")
    @Valid
    private ActiveCurrencyAndAmount wltDayTtlAmtLmt;

    /*
     * 国家和地区代码
     */
    @JacksonXmlProperty(localName = "CtryAndRgnlCd")
    @Length(min = 2, max = 2)
    private String ctryAndRgnlCd;
}
