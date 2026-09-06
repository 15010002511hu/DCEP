package com.dcep.supergw.dto.dc023;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 组件ReleaseInformation
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:39:26
 */
@Data
public class RlsInf implements Serializable {
    /**
     * 合约产品ID
     */
    @JacksonXmlProperty(
            localName = "PdctId"
    )
    @Length(
            min = 1,
            max = 16
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String pdctId;

    /**
     * 产品版本号
     */
    @JacksonXmlProperty(
            localName = "PdctVrsn"
    )
    @Length(
            min = 1,
            max = 11
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String pdctVrsn;

    /**
     * 合约产品版本变更类型
     */
    @JacksonXmlProperty(
            localName = "ChngTp"
    )
    @NotBlank
    @Pattern(regexp = "SCPVCT01||SCPVCT02||SCPVCT03||SCPVCT04||SCPVCT05")
    private String chngTp;
}
