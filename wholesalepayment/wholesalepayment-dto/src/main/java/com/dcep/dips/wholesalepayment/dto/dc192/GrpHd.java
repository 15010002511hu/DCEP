package com.dcep.dips.wholesalepayment.dto.dc192;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件GroupHeader
 * @Author qiaopengyu
 * @date 2025-10-31 11:25:03
 */
@Data
public class GrpHd implements Serializable {
    /**
     * 报文标识号
     */
    @JacksonXmlProperty(
            localName = "MsgId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    private String msgId;

    /**
     * 业务处理时间
     */
    @JacksonXmlProperty(
            localName = "CreDtTm"
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
    private String creDtTm;
}
