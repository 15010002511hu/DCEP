package com.dcep.supergw.dto.dc037;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 签约方商户信息
 *
 * @Author qinchaoyong
 * @date 2024-11-26 14:16:05
 */
@Data
public class SgntrMrchntInf implements Serializable {
    /**
     * 商户ID
     */
    @JacksonXmlProperty(
            localName = "MrchntId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String mrchntId;

    /**
     * 商户名称
     */
    @JacksonXmlProperty(
            localName = "MrchntNm"
    )
    @Length(
            min = 1,
            max = 60
    )
    @NotBlank
    private String mrchntNm;

    /**
     * 商户简称
     */
    @JacksonXmlProperty(
            localName = "MrchntAbbrvtdNm"
    )
    @Length(
            min = 1,
            max = 30
    )
    @NotBlank
    private String mrchntAbbrvtdNm;

    /**
     * 受理机构金融编码
     */
    @JacksonXmlProperty(
            localName = "AcqrgAgtInstnId"
    )
    @Length(
            min = 1,
            max = 14
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String acqrgAgtInstnId;

    /**
     * 受理机构名称
     */
    @JacksonXmlProperty(
            localName = "AcqrgAgtNm"
    )
    @Length(
            min = 1,
            max = 60
    )
    private String acqrgAgtNm;

    /**
     * 商户所属运营机构
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
     * 商户所属运营机构名称
     */
    @JacksonXmlProperty(
            localName = "InstnIdNm"
    )
    @Length(
            min = 1,
            max = 60
    )
    @NotBlank
    private String instnIdNm;
}
