package com.dcep.dips.wholesalepayment.dto.dc114;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件----Envelope
 * @Author zhaotianwu
 * @date 2025-10-13 17:13:26
 */
@Data
public class Envlp implements Serializable {
    /**
     * 组件------Purpose
     */
    @JacksonXmlProperty(
            localName = "Purp"
    )
    @NotNull
    @Valid
    private Purp purp;

    /**
     * 唯一标识号
     */
    @JacksonXmlProperty(
            localName = "UETR"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String uETR;

    /**
     * 渠道类型
     */
    @JacksonXmlProperty(
            localName = "ChanlTp"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String chanlTp;

    /**
     * 批次号
     */
    @JacksonXmlProperty(
            localName = "BtchId"
    )
    @Length(
            min = 1,
            max = 13
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String btchId;

    /**
     * 系统工作日期
     */
    @JacksonXmlProperty(
            localName = "SysWorkDt"
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
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String sysWorkDt;

    /**
     * 结算日期
     */
    @JacksonXmlProperty(
            localName = "SttlmDt"
    )
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd"
    )
    @Pattern(
            regexp = "(\\d{4}-\\d{2}-\\d{2})?",
            message = "日期格式错误，正确格式是：yyyy-MM-dd"
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String sttlmDt;

    /**
     * 附言
     */
    @JacksonXmlProperty(
            localName = "PstScrpt"
    )
    @Length(
            min = 1,
            max = 105
    )
    private String pstScrpt;

    /**
     * 备注
     */
    @JacksonXmlProperty(
            localName = "Rmk"
    )
    @Length(
            min = 1,
            max = 105
    )
    private String rmk;
}
