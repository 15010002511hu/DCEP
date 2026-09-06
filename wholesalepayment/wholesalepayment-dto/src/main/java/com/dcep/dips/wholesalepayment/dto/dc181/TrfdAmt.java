package com.dcep.dips.wholesalepayment.dto.dc181;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--TransferredAmount
 * @Author luteng
 * @date 2025-09-05 16:32:04
 */
@Data
public class TrfdAmt implements Serializable {
    /**
     * 调减金额
     */
    @JacksonXmlProperty(localName = "AmtWthCcy")
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount amtWthCcy;
}
