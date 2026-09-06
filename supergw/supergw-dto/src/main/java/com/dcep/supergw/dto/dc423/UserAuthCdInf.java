package com.dcep.supergw.dto.dc423;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 授权码信息
 * @Author qinchaoyong
 * @date 2025-01-15 15:06:27
 */
@Data
public class UserAuthCdInf implements Serializable {
    /**
     * 授权码
     */
    @JacksonXmlProperty(
        localName = "UserAuthCd"
    )
    @Length(
        min = 1,
        max = 34
    )
    @NotBlank
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    private String userAuthCd;
}
