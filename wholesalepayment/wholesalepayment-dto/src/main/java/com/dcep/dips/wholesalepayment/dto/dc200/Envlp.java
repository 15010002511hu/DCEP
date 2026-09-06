package com.dcep.dips.wholesalepayment.dto.dc200;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件----Envelope
 * @Author luteng
 * @date 2025-09-28 11:40:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envlp implements Serializable {
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
    @NotBlank
    @Pattern(
            regexp = "(\\d{4}-\\d{2}-\\d{2})?",
            message = "日期格式错误，正确格式是：yyyy-MM-dd"
    )
    private String sttlmDt;
}
