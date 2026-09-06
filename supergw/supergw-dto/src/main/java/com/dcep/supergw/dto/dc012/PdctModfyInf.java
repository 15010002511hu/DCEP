package com.dcep.supergw.dto.dc012;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 组件ProductModifyInformation
 *
 * @Author qinchaoyong
 * @date 2024-09-10 11:57:50
 */
@Data
public class PdctModfyInf implements Serializable {
    /**
     * 申请流水号
     */
    @JacksonXmlProperty(
            localName = "SrlNb"
    )
    @Length(
            min = 1,
            max = 64
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String srlNb;
}
