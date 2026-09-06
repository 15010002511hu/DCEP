package com.dcep.supergw.dto.dc014;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 组件ProductLogOutInformation
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:08:35
 */
@Data
public class PdctLgOutInf implements Serializable {
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
     * 变更前状态
     */
    @JacksonXmlProperty(
            localName = "StsBfrChng"
    )
    @NotBlank
    @Pattern(regexp = "SCPS01||SCPS02||SCPS03||SCPS04")
    private String stsBfrChng;

    /**
     * 变更后状态
     */
    @JacksonXmlProperty(
            localName = "StsAftrChng"
    )
    @NotBlank
    @Pattern(regexp = "SCPS01||SCPS02||SCPS03||SCPS04")
    private String stsAftrChng;
}
