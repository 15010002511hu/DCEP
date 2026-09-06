package com.dcep.supergw.dto.dc425;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 补缴信息
 *
 * @Author qinchaoyong
 * @date 2025-03-17 16:22:46
 */
@Data
public class PdInf implements Serializable {
    /**
     * 补缴金额
     */
    @JacksonXmlProperty(
            localName = "PdAmt"
    )
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount pdAmt;

    /**
     * 补缴支付方式说明
     */
    @JacksonXmlProperty(
            localName = "Pstscrpt"
    )
    @Length(
            min = 1,
            max = 120
    )
    @NotBlank
    private String pstscrpt;
}
