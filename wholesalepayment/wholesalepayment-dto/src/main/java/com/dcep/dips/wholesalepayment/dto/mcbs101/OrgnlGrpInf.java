package com.dcep.dips.wholesalepayment.dto.mcbs101;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件--OriginalGroupInformation
 * @Author luzhikuan
 * @date 2025-10-03 15:58:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgnlGrpInf implements Serializable {
    /**
     * 原报文标识号
     */
    @JacksonXmlProperty(
            localName = "OrgnlMsgId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String orgnlMsgId;

    /**
     * 原报文类型
     */
    @JacksonXmlProperty(
            localName = "OrgnlMsgNmId"
    )
    @Length(
            min = 1,
            max = 15
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String orgnlMsgNmId;
}
