package com.dcep.supergw.dto.dc068;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 补贴发放列表
 *
 * @Author qinchaoyong
 * @date 2025-03-03 10:40:19
 */
@Data
public class SubDist implements Serializable {
    /**
     * 补贴处理状态
     */
    @JacksonXmlProperty(
            localName = "DistSts"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String distSts;

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
     * 钱包类型
     */
    @JacksonXmlProperty(
            localName = "WltTp"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String wltTp;

    /**
     * 钱包等级
     */
    @JacksonXmlProperty(
            localName = "WltLvl"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String wltLvl;

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
}
