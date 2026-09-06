package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件------PaymentIdentification
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:28:05
 */
@Data
public class PmtId implements Serializable {
    /**
     * 唯一标识号
     */
    @JacksonXmlProperty(
            localName = "UETR"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String uETR;
}
