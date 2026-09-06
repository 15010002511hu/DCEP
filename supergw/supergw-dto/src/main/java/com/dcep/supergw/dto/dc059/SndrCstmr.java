package com.dcep.supergw.dto.dc059;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 发起方客户
 *
 * @Author qinchaoyong
 * @date 2024-11-22 09:26:25
 */
@Data
public class SndrCstmr implements Serializable {
    /**
     * 发起方客户运营机构
     */
    @JacksonXmlProperty(
            localName = "SndrPty"
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
    private String sndrPty;

    /**
     * 客户名称
     */
    @JacksonXmlProperty(
            localName = "SndrCstmrNm"
    )
    @Length(
            min = 1,
            max = 240
    )
    private String sndrCstmrNm;

    /**
     * 钱包ID
     */
    @JacksonXmlProperty(
            localName = "SndrWltId"
    )
    @Length(
            min = 1,
            max = 68
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String sndrWltId;

    /**
     * 钱包等级
     */
    @JacksonXmlProperty(
            localName = "SndrWltLvl"
    )
    @Pattern(
            regexp = "WL01||WL02||WL03||WL04"
    )
    private String sndrWltLvl;

    /**
     * 钱包类型
     */
    @JacksonXmlProperty(
            localName = "SndrWltTp"
    )
    @Pattern(regexp = "WT[0-9]{2}")
    private String sndrWltTp;

    /**
     * 钱包名称
     */
    @JacksonXmlProperty(
            localName = "SndrWltNm"
    )
    @Length(
            min = 1,
            max = 60
    )
    private String sndrWltNm;
}
