package com.dcep.supergw.dto.dc008;

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
 * 组件--ProductVersionParameterList
 *
 * @Author qinchaoyong
 * @date 2024-09-10 11:44:59
 */
@Data
public class PdctVrsnParam implements Serializable {
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
     * 创建时间
     */
    @JacksonXmlProperty(
            localName = "CretTm"
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
    private String cretTm;

    /**
     * 发布时间
     */
    @JacksonXmlProperty(
            localName = "PblshTm"
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
    private String pblshTm;

    /**
     * 版本描述
     */
    @JacksonXmlProperty(
            localName = "PdctVrsnDesc"
    )
    @Length(
            min = 1,
            max = 400
    )
    @NotBlank
    private String pdctVrsnDesc;

    /**
     * 产品版本参数
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "PdctVrsnParams")
    @NotNull
    @Valid
    private List<PdctVrsnParams> pdctVrsnParams;
}
