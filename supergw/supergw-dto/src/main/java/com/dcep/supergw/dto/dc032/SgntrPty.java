package com.dcep.supergw.dto.dc032;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 签约方列表
 *
 * @Author qinchaoyong
 * @date 2024-09-11 09:52:21
 */
@Data
public class SgntrPty implements Serializable {
    /**
     * 签约方序号
     */
    @JacksonXmlProperty(
            localName = "SgntrPtyNb"
    )
    @Length(
            min = 1,
            max = 2
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String sgntrPtyNb;

    /**
     * 签约方类型
     */
    @JacksonXmlProperty(
            localName = "SgntrPtyTp"
    )
    @NotBlank
    @Pattern(regexp = "SCPT01||SCPT02||SCPT03||SCPT04")
    private String sgntrPtyTp;
}
