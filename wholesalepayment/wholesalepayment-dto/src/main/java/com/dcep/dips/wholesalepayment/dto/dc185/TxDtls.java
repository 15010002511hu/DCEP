package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * 组件------TransactionDetails
 * @Author luteng
 * @date 2025-10-12 16:16:54
 */
@Data
public class TxDtls implements Serializable {
    /**
     * 参考信息
     */
    @JacksonXmlProperty(
            localName = "Refs"
    )
    @Valid
    private Refs refs;
}
