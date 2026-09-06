package com.dcep.dips.wholesalepayment.dto.dc428;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件----OriginalGroupInformation
 * @Author luteng
 * @date 2025-10-21 17:29:41
 */
@Data
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
     * 原报文编号
     */
    @JacksonXmlProperty(
            localName = "OrgnlMsgNmId"
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
    private String orgnlMsgNmId;
}
