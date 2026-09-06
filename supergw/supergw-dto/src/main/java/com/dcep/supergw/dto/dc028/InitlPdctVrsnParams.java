package com.dcep.supergw.dto.dc028;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 首个产品版本参数
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:51:53
 */
@Data
public class InitlPdctVrsnParams implements Serializable {
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
     * 协议文本
     */
    @JacksonXmlProperty(
            localName = "Agrmt"
    )
    @NotNull
    @Valid
    private Agrmt agrmt;

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
}
