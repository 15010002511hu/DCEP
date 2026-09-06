package com.dcep.dips.wholesalepayment.dto.dc112;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件----Envelope
 * @Author zhaotianwu
 * @date 2025-10-13 16:55:42
 */
@Data
public class Envlp implements Serializable {
    /**
     * 渠道类型
     */
    @JacksonXmlProperty(
            localName = "ChanlTp"
    )
    @NotBlank
    private String chanlTp;

    /**
     * 交易批次号
     */
    @JacksonXmlProperty(
            localName = "BatchId"
    )
    @Length(
            min = 1,
            max = 13
    )
    @NotBlank
    private String batchId;

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
    private String sttlmDt;
}
