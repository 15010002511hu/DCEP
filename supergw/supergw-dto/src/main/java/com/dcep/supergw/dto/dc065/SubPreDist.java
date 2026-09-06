package com.dcep.supergw.dto.dc065;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 补贴预发放列表
 *
 * @Author qinchaoyong
 * @date 2025-03-03 09:39:43
 */
@Data
public class SubPreDist implements Serializable {
    /**
     * 客户证件类型
     */
    @JacksonXmlProperty(
            localName = "CustIdTp"
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String custIdTp;

    /**
     * 客户证件编码
     */
    @JacksonXmlProperty(
            localName = "CustIdCd"
    )
    @Length(
            min = 1,
            max = 64
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String custIdCd;

    /**
     * 客户名称
     */
    @JacksonXmlProperty(
            localName = "CustNm"
    )
    @Length(
            min = 1,
            max = 240
    )
    private String custNm;

    /**
     * 预发放领取方式
     */
    @JacksonXmlProperty(
            localName = "PreDistCollMethod"
    )
    @Length(
            min = 1,
            max = 70
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String preDistCollMethod;

    /**
     * 钱包所属运营机构
     */
    @JacksonXmlProperty(
            localName = "InstnId"
    )
    @Length(
            min = 1,
            max = 14
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String instnId;

    /**
     * 钱包等级
     */
    @JacksonXmlProperty(
            localName = "WltLvl"
    )
    @Pattern(
            regexp = "WL[0-9]{2}"
    )
    private String wltLvl;

    /**
     * 绑卡银行编码
     */
    @JacksonXmlProperty(
            localName = "BkBindCd"
    )
    @Length(
            min = 1,
            max = 14
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String bkBindCd;

    @JacksonXmlProperty(localName = "SubAmt")
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount subAmt;

    @JacksonXmlProperty(localName = "SubNm")
    @NotBlank
    @Length(min = 1,max = 24)
    private String subNm;

    @JacksonXmlProperty(localName = "SubTp")
    @NotBlank
    @Pattern(regexp = "RPT[0-9]{2}")
    private String subTp;

    @JacksonXmlProperty(localName = "UseLimit")
    @Length(min = 1,max = 22)
    private String useLimit;

    @JacksonXmlProperty(localName = "UseLimitUrl")
    @Length(min = 1,max = 1024)
    private String UseLimitUrl;
}
