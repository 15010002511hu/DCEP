package com.dcep.supergw.dto.dc425;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 原交易信息
 *
 * @Author qinchaoyong
 * @date 2025-03-17 16:22:46
 */
@Data
public class OrgnlTxInf implements Serializable {
    /**
     * 订单号
     */
    @JacksonXmlProperty(
            localName = "OrdrNo"
    )
    @Length(
            min = 1,
            max = 64
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String ordrNo;

    /**
     * 订单金额
     */
    @JacksonXmlProperty(
            localName = "OrdrAmt"
    )
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount ordrAmt;

    /**
     * 收款运营机构
     */
    @JacksonXmlProperty(
            localName = "CdtrPtyId"
    )
    @Length(
            min = 1,
            max = 14
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String cdtrPtyId;
}
