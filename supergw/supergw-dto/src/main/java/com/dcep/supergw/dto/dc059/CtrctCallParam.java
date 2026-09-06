package com.dcep.supergw.dto.dc059;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 合约参数
 *
 * @Author qinchaoyong
 * @date 2024-11-22 09:26:21
 */
@Data
public class CtrctCallParam implements Serializable {
    /**
     * 调用路径
     */
    @JacksonXmlProperty(
            localName = "CallPth"
    )
    @Length(
            min = 1,
            max = 128
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String callPth;

    /**
     * 调用参数
     */
    @JacksonXmlProperty(
            localName = "CallParam"
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String callParam;
}
