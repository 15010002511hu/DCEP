package com.dcep.supergw.dto.dc038;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 签约方列表
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:19:59
 */
@Data
public class SgntrPty implements Serializable {
    /**
     * 签约方意愿
     */
    @JacksonXmlProperty(
            localName = "SgntrIntntn"
    )
    @Pattern(
            regexp = "SCPI01||SCPI02||SCPI03"
    )
    private String sgntrIntntn;

    /**
     * 签约方意愿确认时间
     */
    @JacksonXmlProperty(
            localName = "SgntrIntntnTm"
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
    private String sgntrIntntnTm;

    /**
     * 签约方钱包信息
     */
    @JacksonXmlProperty(
            localName = "SgntrWltInf"
    )
    @Valid
    private SgntrWltInf sgntrWltInf;
}
