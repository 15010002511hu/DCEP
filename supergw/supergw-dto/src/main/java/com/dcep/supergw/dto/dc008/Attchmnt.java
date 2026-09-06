package com.dcep.supergw.dto.dc008;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 附件
 *
 * @Author qinchaoyong
 * @date 2024-09-10 11:44:59
 */
@Data
public class Attchmnt implements Serializable {
    /**
     * 附件地址
     */
    @JacksonXmlProperty(
            localName = "URL"
    )
    @Length(
            min = 1,
            max = 256
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String uRL;

    @JacksonXmlProperty(
        localName = "Hash"
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
    private String hash;
}
