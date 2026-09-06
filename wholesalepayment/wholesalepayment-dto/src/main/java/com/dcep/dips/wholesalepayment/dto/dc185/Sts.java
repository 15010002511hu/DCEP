package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件----Status
 * @Author luteng
 * @date 2025-10-12 16:16:56
 */
@Data
public class Sts implements Serializable {
    /**
     * 业务状态
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
            regexp = "PR10"
    )
    private String prtry;
}
