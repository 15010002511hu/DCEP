package com.dcep.supergw.dto.dc066;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 补贴预发放列表
 *
 * @Author qinchaoyong
 * @date 2025-03-03 10:35:38
 */
@Data
public class SubPreDist implements Serializable {
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
     * 预发放处理结果
     */
    @JacksonXmlProperty(
            localName = "PreDistProcRlt"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String preDistProcRlt;

    /**
     * 预发放处理说明
     */
    @JacksonXmlProperty(
            localName = "PreDistProcDesc"
    )
    @Length(
            min = 1,
            max = 5
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    @Pattern(
            regexp = "R412"
    )
    private String preDistProcDesc;

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
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String wltId;

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
    private String wltNm;

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
