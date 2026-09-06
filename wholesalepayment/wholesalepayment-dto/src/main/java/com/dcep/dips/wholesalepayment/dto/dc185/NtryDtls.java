package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 详情
 * @Author luteng
 * @date 2025-10-12 16:16:55
 */
@Data
public class NtryDtls implements Serializable {
    /**
     * 组件------TransactionDetails
     */
    @JacksonXmlProperty(
            localName = "TxDtls"
    )
    @NotNull
    @Valid
    private TxDtls txDtls;
}
