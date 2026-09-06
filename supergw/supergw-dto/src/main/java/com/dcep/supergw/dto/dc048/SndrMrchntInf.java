package com.dcep.supergw.dto.dc048;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 商户信息
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:38:54
 */
@Data
public class SndrMrchntInf implements Serializable {
    /**
     * 商户号
     */
    @JacksonXmlProperty(
            localName = "MrchntNb"
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
    private String mrchntNb;

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
     * 受理服务机构编码
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
     * 受理服务机构名称
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
     * 商户所属运营机构编码
     */
    @JacksonXmlProperty(
            localName = "CdtrInstnId"
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
    private String cdtrInstnId;

    /**
     * 商户所属运营机构名称
     */
    @JacksonXmlProperty(
            localName = "CdtrInstnNm"
    )
    @Length(
            min = 1,
            max = 60
    )
    @NotBlank
    private String cdtrInstnNm;
}
