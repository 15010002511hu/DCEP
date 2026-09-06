package com.dcep.supergw.dto.dc008;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 产品版本参数
 *
 * @Author qinchaoyong
 * @date 2024-09-10 11:44:57
 */
@Data
public class PdctVrsnParams implements Serializable {
    /**
     * 参数标识
     */
    @JacksonXmlProperty(
            localName = "ParamLbl"
    )
    @Length(
            min = 1,
            max = 64
    )
    @NotBlank
    private String paramLbl;

    /**
     * 参数值
     */
    @JacksonXmlProperty(
            localName = "ParamVal"
    )
    @Length(
            min = 1,
            max = 64
    )
    @NotBlank
    private String paramVal;

    /**
     * 参数类型
     */
    @JacksonXmlProperty(
            localName = "ParamTp"
    )
    @Length(
            min = 1,
            max = 32
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String paramTp;


    @Valid
    @JacksonXmlProperty(localName = "PmtMsg")
    PmtMsg pmtMsg;


}
