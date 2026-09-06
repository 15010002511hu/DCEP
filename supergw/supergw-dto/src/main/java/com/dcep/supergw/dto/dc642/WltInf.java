/*
 * pbcdci.cn Inc. Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc642;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 钱包信息
 * 
 * @author laimincai
 * @date 2024/04/24
 */

@Getter
@Setter
@ToString
public class WltInf implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2535566837441824825L;

    /**
     * APDU数据信息
     */
    @JacksonXmlProperty(localName = "APDU")
    @Length(min = 1, max = 1024)
    private String apdu;

    /**
     * 硬钱包余额
     */
    @JacksonXmlProperty(localName = "WltBal")
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount wltBal;

    /**
     * 硬钱包红包余额
     */
    @JacksonXmlProperty(localName = "RedPktBal")
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount redPktBal;
}
