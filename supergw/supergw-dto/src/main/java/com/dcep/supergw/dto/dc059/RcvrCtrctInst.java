package com.dcep.supergw.dto.dc059;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 接收方合约实例
 *
 * @Author qinchaoyong
 * @date 2024-11-22 09:26:21
 */
@Data
public class RcvrCtrctInst implements Serializable {
    /**
     * 合约实例ID
     */
    @JacksonXmlProperty(
            localName = "RcvrCtrctInstId"
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
    private String rcvrCtrctInstId;

    /**
     * 合约产品ID
     */
    @JacksonXmlProperty(
            localName = "RcvrPdctId"
    )
    @Length(
            min = 1,
            max = 16
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String rcvrPdctId;
}
