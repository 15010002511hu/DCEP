package com.dcep.dips.wholesalepayment.dto.dc192;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件----Reason
 * @Author qiaopengyu
 * @date 2025-10-31 11:24:55
 */
@Data
public class Rsn implements Serializable {
    /**
     * 业务处理码
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
