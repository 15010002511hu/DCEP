package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件----BookingDate
 * @Author luteng
 * @date 2025-10-12 16:16:55
 */
@Data
public class BookgDt implements Serializable {
    /**
     * 记账日期
     */
    @JacksonXmlProperty(
            localName = "Dt"
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
    private String dt;
}
