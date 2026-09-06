package com.dcep.supergw.dto.dc018;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 组件ProductVersionDetailInformation
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:31:24
 */
@Data
public class PdctVrsnDtlInf implements Serializable {
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
     * 合约模板ID
     */
    @JacksonXmlProperty(
            localName = "TmpltId"
    )
    @Length(
            min = 1,
            max = 64
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String tmpltId;

    /**
     * 模板版本号
     */
    @JacksonXmlProperty(
            localName = "TmpltVrsn"
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
    private String tmpltVrsn;

    /**
     * 用户协议
     */
    @JacksonXmlProperty(
            localName = "UsrAgrmt"
    )
    @NotNull
    @Valid
    private UsrAgrmt usrAgrmt;

    /**
     * 版本描述
     */
    @JacksonXmlProperty(
            localName = "TmpltVrsnDesc"
    )
    @Length(
            min = 1,
            max = 400
    )
    @NotBlank
    private String tmpltVrsnDesc;

    /**
     * 版本变更类型
     */
    @JacksonXmlProperty(
            localName = "VrsnChngTp"
    )
    @NotBlank
    @Pattern(regexp = "SCPVCT01||SCPVCT02||SCPVCT03||SCPVCT04||SCPVCT05")
    private String vrsnChngTp;

    /**
     * 产品版本参数
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "PdctVrsnParams")
    @Valid
    private List<PdctVrsnParams> pdctVrsnParams;

    /**
     * 版本状态
     */
    @JacksonXmlProperty(
            localName = "VrsnSts"
    )
    @NotBlank
    @Pattern(regexp = "SCPVS01||SCPVS02||SCPVS03||SCPVS04||SCPVS05||SCPVS06")
    private String vrsnSts;

    /**
     * 版本创建时间
     */
    @JacksonXmlProperty(
            localName = "VrsnCretTm"
    )
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    @NotBlank
    @Pattern(
            regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})?",
            message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss"
    )
    private String vrsnCretTm;

    /**
     * 版本提审时间
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

    /**
     * 版本审核时间
     */
    @JacksonXmlProperty(
            localName = "VrsnAudtTm"
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
    private String vrsnAudtTm;

    /**
     * 版本发布时间
     */
    @JacksonXmlProperty(
            localName = "VrsnPblshTm"
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
    private String vrsnPblshTm;

    /**
     * 版本删除时间
     */
    @JacksonXmlProperty(
            localName = "VrsnDelTm"
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
    private String vrsnDelTm;
}
