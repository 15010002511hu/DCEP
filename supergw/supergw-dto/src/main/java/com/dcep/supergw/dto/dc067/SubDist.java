package com.dcep.supergw.dto.dc067;

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
 * 补贴发放信息列表
 *
 * @Author qinchaoyong
 * @date 2025-03-03 09:59:38
 */
@Data
public class SubDist implements Serializable {
    /**
     * 钱包ID
     */
    @JacksonXmlProperty(
            localName = "WltId"
    )
    @Length(
            min = 1,
            max = 68
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String wltId;

    @JacksonXmlProperty(
        localName = "CtrctId"
    )
    @Length(
        min = 1,
        max = 64
    )
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    private String ctrctId;

    /**
     * 钱包名称
     */
    @JacksonXmlProperty(
            localName = "WltNm"
    )
    @Length(
            min = 1,
            max = 60
    )
    @NotBlank
    private String wltNm;

    /**
     * 补贴金额
     */
    @JacksonXmlProperty(
            localName = "SubAmt"
    )
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount subAmt;

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
     * 钱包类型
     */
    @JacksonXmlProperty(
            localName = "WltTp"
    )
    @Pattern(regexp = "WT[0-9]{2}")
    private String wltTp;

    /**
     * 钱包等级
     */
    @JacksonXmlProperty(
            localName = "WltLvl"
    )
    @Pattern(
            regexp = "WL01||WL02||WL03||WL04"
    )
    private String wltLvl;
}
