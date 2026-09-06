package com.dcep.dips.wholesalepayment.dto.mcbs102;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @date 2025-10-18 15:58:33
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

    /**
     * 清零日期
     */
    @JacksonXmlProperty(
            localName = "ClrZeDt"
    )
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd"
    )
    @NotBlank
    @Pattern(
            regexp = "(\\d{4}-\\d{2}-\\d{2})?",
            message = "日期格式错误，正确格式是：yyyy-MM-dd"
    )
    private String ClrZeDt;
}
