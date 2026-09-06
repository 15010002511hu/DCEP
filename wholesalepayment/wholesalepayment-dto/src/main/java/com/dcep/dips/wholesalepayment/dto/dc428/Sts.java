package com.dcep.dips.wholesalepayment.dto.dc428;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件Status
 * @Author luteng
 * @date 2025-10-21 17:29:46
 */
@Data
@AllArgsConstructor
public class Sts implements Serializable {
    /**
     * 撤销处理状态
     */
    @JacksonXmlProperty(
            localName = "Conf"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    @Pattern(
            regexp = "PR00||PR01"
    )
    private String conf;
}
