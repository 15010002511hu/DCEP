package com.dcep.supergw.dto.dc030;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 审核信息
 *
 * @Author qinchaoyong
 * @date 2024-09-11 09:48:35
 */
@Data
public class AudtInf implements Serializable {
    /**
     * 审核人
     */
    @JacksonXmlProperty(
            localName = "Audtr"
    )
    @Length(
            min = 1,
            max = 20
    )
    private String audtr;

    /**
     * 提审时间
     */
    @JacksonXmlProperty(
            localName = "ArrgmtTm"
    )
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    @NotBlank
    @Pattern(
            regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})?",
            message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss"
    )
    private String arrgmtTm;

    /**
     * 审核时间
     */
    @JacksonXmlProperty(
            localName = "AudtTm"
    )
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    @Pattern(
            regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})?",
            message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss"
    )
    private String audtTm;

    /**
     * 审核意见
     */
    @JacksonXmlProperty(
            localName = "AudtView"
    )
    @Length(
            min = 1,
            max = 400
    )
    private String audtView;

    /**
     * 审核状态
     */
    @JacksonXmlProperty(
            localName = "AudtSts"
    )
    @Pattern(regexp = "BAS01||BAS02||BAS03")
    private String audtSts;
}
