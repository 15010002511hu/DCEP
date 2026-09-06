package com.dcep.dips.wholesalepayment.dto.dc428;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件Assignment
 * @Author luteng
 * @date 2025-10-21 17:29:50
 */
@Data
public class Assgnmt implements Serializable {
    /**
     * 报文标识号
     */
    @JacksonXmlProperty(
            localName = "Id"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    private String id;

    /**
     * 组件--Assigner
     */
    @JacksonXmlProperty(
            localName = "Assgnr"
    )
    @NotNull
    @Valid
    private Assgnr assgnr;

    /**
     * 接收参与机构
     */
    @JacksonXmlProperty(
            localName = "Assgne"
    )
    @NotNull
    @Valid
    private Assgne assgne;

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
