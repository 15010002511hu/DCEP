package com.dcep.supergw.dto.dc048;

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
 * @date 2024-09-11 10:38:58
 */
@Data
public class CtrctPty implements Serializable {
    /**
     * 签约方意愿
     */
    @JacksonXmlProperty(
            localName = "CtrctPtyWill"
    )
    @Pattern(
            regexp = "SCPI01||SCPI02||SCPI03"
    )
    private String ctrctPtyWill;

    /**
     * 意愿确认时间
     */
    @JacksonXmlProperty(
            localName = "WillCnfrmgTm"
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
    private String willCnfrmgTm;

    /**
     * 签约方钱包信息
     */
    @JacksonXmlProperty(
            localName = "WltInf"
    )
    @Valid
    private WltInf wltInf;

    /**
     * 商户信息
     */
    @JacksonXmlProperty(
            localName = "SndrMrchntInf"
    )
    @Valid
    private SndrMrchntInf sndrMrchntInf;

    /**
     * 签约方机构信息
     */
    @JacksonXmlProperty(
            localName = "CtrctPtyInf"
    )
    @Valid
    private CtrctPtyInf ctrctPtyInf;
}
