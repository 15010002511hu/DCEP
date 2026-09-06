package com.dcep.dips.wholesalepayment.dto.dc185;

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
 * 组件--Entry
 * @Author luteng
 * @date 2025-10-12 16:17:01
 */
@Data
public class Ntry implements Serializable {
    /**
     * 条目参考
     */
    @JacksonXmlProperty(
            localName = "NtryRef"
    )
    @Length(
            min = 1,
            max = 35
    )
    private String ntryRef;

    /**
     * 调整金额
     */
    @JacksonXmlProperty(
            localName = "Amt"
    )
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount amt;

    /**
     * 借贷记标识
     */
    @JacksonXmlProperty(
            localName = "CdtDbtInd"
    )
    @NotBlank
    @Pattern(
            regexp = "CRDT||DBIT"
    )
    private String cdtDbtInd;

    /**
     * 组件----Status
     */
    @JacksonXmlProperty(
            localName = "Sts"
    )
    @NotNull
    @Valid
    private Sts sts;

    /**
     * 组件----BankTransactionCode
     */
    @JacksonXmlProperty(
            localName = "BkTxCd"
    )
    @NotNull
    @Valid
    private BkTxCd bkTxCd;

    /**
     * 组件----BookingDate
     */
    @JacksonXmlProperty(
            localName = "BookgDt"
    )
    @NotNull
    @Valid
    private BookgDt bookgDt;

    /**
     * 详情
     */
    @JacksonXmlProperty(
            localName = "NtryDtls"
    )
    @NotNull
    @Valid
    private NtryDtls ntryDtls;
}
