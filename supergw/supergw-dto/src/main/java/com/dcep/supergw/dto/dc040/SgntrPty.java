package com.dcep.supergw.dto.dc040;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * 签约方列表
 *
 * @Author qinchaoyong
 * @date 2024-09-11 14:12:06
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

    @JacksonXmlProperty(
        localName = "SgntrWltInf"
    )
    @Valid
    SgntrWltInf sgntrWltInf;

    @JacksonXmlProperty(
        localName = "SgntrMrchntInf"
    )
    @Valid
    SgntrMrchntInf sgntrMrchntInf;

    @JacksonXmlProperty(
        localName = "SgntrInstnInf"
    )
    @Valid
    SgntrInstnInf sgntrInstnInf;
}
