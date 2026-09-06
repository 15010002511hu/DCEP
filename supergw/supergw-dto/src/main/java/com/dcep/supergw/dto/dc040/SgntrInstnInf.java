package com.dcep.supergw.dto.dc040;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件--SignatureInstitutionInformation签约方机构信息
 *
 * @Author qinchaoyong
 * @date 2024-09-11 14:12:06
 */
@Data
public class SgntrInstnInf implements Serializable {
    /**
     * 签约方机构类型
     */
    @JacksonXmlProperty(
            localName = "SgntrInstnTp"
    )
    @NotBlank
    @Pattern(
            regexp = "IST[0-9]{2}"
    )
    private String sgntrInstnTp;

    /**
     * 签约方一级机构编码
     */
    @JacksonXmlProperty(
            localName = "SgntrPmryInstnId"
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
    private String sgntrPmryInstnId;

    /**
     * 签约方一级机构名称
     */
    @JacksonXmlProperty(
            localName = "SgntrPmryInstnNm"
    )
    @Length(
            min = 1,
            max = 60
    )
    @NotBlank
    private String sgntrPmryInstnNm;

    /**
     * 签约方二级机构编码
     */
    @JacksonXmlProperty(
            localName = "SgntrScndryInstn"
    )
    @Length(
            min = 1,
            max = 32
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String sgntrScndryInstn;

    /**
     * 签约方二级机构名称
     */
    @JacksonXmlProperty(
            localName = "SgntrScndryInstnNm"
    )
    @Length(
            min = 1,
            max = 60
    )
    private String sgntrScndryInstnNm;
}
