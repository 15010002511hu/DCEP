package com.dcep.supergw.dto.dc022;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 组件ProductVersionCreateInformation
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:38:52
 */
@Data
public class PdctVrsnCretInf implements Serializable {
    /**
     * 申请流水号
     */
    @JacksonXmlProperty(
            localName = "SrlNb"
    )
    @Length(
            min = 1,
            max = 64
    )
    @NotBlank
    private String srlNb;

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
    @NotBlank
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
     * 产品版本状态
     */
    @JacksonXmlProperty(
            localName = "VrsnSts"
    )
    @NotNull
    @Pattern(regexp = "SCPVS01||SCPVS02||SCPVS03||SCPVS04||SCPVS05||SCPVS06")
    @Valid
    private String vrsnSts;

    /**
     * 产品版本提审时间
     */
    @JacksonXmlProperty(
            localName = "VrsnArrgmtTm"
    )
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    @Pattern(
            regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})?",
            message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss"
    )
    private String vrsnArrgmtTm;
}
