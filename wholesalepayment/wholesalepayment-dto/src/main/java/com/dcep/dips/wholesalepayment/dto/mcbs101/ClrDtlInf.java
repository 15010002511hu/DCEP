package com.dcep.dips.wholesalepayment.dto.mcbs101;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 清零结果
 * @author: luzhikuan
 * @create: 2025-10-03 15:46:24
 */
@Data
public class ClrDtlInf implements Serializable {
    /**
     * 机构标识组件
     */
    @JacksonXmlProperty(
            localName = "FinInsTnId"
    )
    @NotNull
    @Valid
    private FinInstnId finInsTnId;

    /**
     * 完成清零余额
     */
    @JacksonXmlProperty(
            localName = "FishClrAmt"
    )
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount fishClrAmt;

    /**
     * 未完成清零余额
     */
    @JacksonXmlProperty(
            localName = "UnFishClrAmt"
    )
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount unFishClrAmt;

    /**
     *
     */
    @JacksonXmlProperty(
            localName = "CtgyPurp"
    )
    @NotNull
    @Valid
    private CtgyPurp ctgyPurp;

    /**
     *
     */
    @JacksonXmlProperty(
            localName = "Envlp"
    )
    @NotNull
    @Valid
    private Envlp envlp;
}
