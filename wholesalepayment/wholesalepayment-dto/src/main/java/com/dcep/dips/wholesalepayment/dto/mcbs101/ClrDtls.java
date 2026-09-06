package com.dcep.dips.wholesalepayment.dto.mcbs101;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 清零信息
 * @Author luzhikuan
 * @date 2025-10-03 15:36:25
 */
@Data
public class ClrDtls implements Serializable {

    /**
     * 清零信息
     */
    @JacksonXmlProperty(
            localName = "ClrDtlInf"
    )
    @NotNull
    @Valid
    private List<ClrDtlInf> clrDtlInf;

    /**
     * 清零日期
     */
    @JacksonXmlProperty(
            localName = "ClrZeDt"
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
    private String clrZeDt;
}
