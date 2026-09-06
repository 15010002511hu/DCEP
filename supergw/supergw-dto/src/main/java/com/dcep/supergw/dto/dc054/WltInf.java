package com.dcep.supergw.dto.dc054;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 签约方钱包信息
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:49:22
 */
@Data
public class WltInf implements Serializable {
    /**
     * 客户名称
     */
    @JacksonXmlProperty(
            localName = "CstmrNm"
    )
    @Length(
            min = 1,
            max = 240
    )
    private String cstmrNm;

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

    /**
     * 合约实例关联钱包
     */
    @JacksonXmlProperty(
            localName = "AssoctnWltFlg"
    )
    @Length(
            min = 1,
            max = 5
    )
    @NotBlank
    @Pattern(
            regexp = "YES||NO"
    )
    private String assoctnWltFlg;

    /**
     * 钱包类型
     */
    @JacksonXmlProperty(
            localName = "WltTp"
    )
    @NotBlank
    @Pattern(regexp = "WT[0-9]{2}")
    private String wltTp;

    /**
     * 钱包等级
     */
    @JacksonXmlProperty(
            localName = "WltLvl"
    )
    @NotBlank
    @Pattern(
            regexp = "WL01||WL02||WL03||WL04"
    )
    private String wltLvl;

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
     * 钱包所属运营机构
     */
    @JacksonXmlProperty(
            localName = "WltPty"
    )
    @Length(
            min = 1,
            max = 14
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    @NotBlank
    private String wltPty;
}
