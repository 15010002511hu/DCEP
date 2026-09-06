package com.dcep.dips.wholesalepayment.dto.dc191;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件--SettlementTimeIndication
 * @Author qiaopengyu
 * @date 2025-10-30 21:17:03
 */
@Data
public class SttlmTmIndctn implements Serializable {
    /**
     * 结算时间
     */
    @JacksonXmlProperty(
            localName = "DbtDtTm"
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
    private String dbtDtTm;
}
