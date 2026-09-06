package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * 组件--------Code
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:28:07
 */
@Data
public class Cd implements Serializable {
    /**
     * 原业务状态
     */
    @JacksonXmlProperty(
            localName = "Prtry"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    @Pattern(
            regexp = "PR00||PR01||PR02"
    )
    private String prtry;
}
