package com.dcep.dips.wholesalepayment.dto.dc428;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件------Reason
 * @Author luteng
 * @date 2025-10-21 17:29:39
 */
@Data
@AllArgsConstructor
public class Rsn implements Serializable {
    /**
     * 原业务处理码
     */
    @JacksonXmlProperty(
            localName = "Prtry"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String prtry;
}
