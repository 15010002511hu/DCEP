package com.dcep.supergw.dto.dc424;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件ResponsionInformation
 * @Author qinchaoyong
 * @date 2025-01-15 15:05:33
 */
@Data
public class RspsnInf implements Serializable {
    /**
     * 业务回执状态
     */
    @JacksonXmlProperty(
        localName = "RspsnSts"
    )
    @NotBlank
    @Pattern(
        regexp = "PR00||PR01"
    )
    private String rspsnSts;

    /**
     * 业务拒绝码
     */
    @JacksonXmlProperty(
        localName = "RjctCd"
    )
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    private String rjctCd;

    /**
     * 业务拒绝信息
     */
    @JacksonXmlProperty(
        localName = "RjctInf"
    )
    @Length(
        min = 1,
        max = 105
    )
    private String rjctInf;

    /**
     * 用户手机号标识
     */
    @JacksonXmlProperty(
        localName = "PhneNbHash"
    )
    @Length(
        min = 1,
        max = 44
    )
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    private String phneNbHash;
}
