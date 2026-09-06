package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * 组件------TransactionOrError
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:28:05
 */
@Data
public class TxOrErr implements Serializable {
    /**
     * 组件--------Transaction
     */
    @JacksonXmlProperty(
            localName = "Tx"
    )
    @Valid
    private Tx tx;

    /**
     * 组件--------BusinessError
     */
    @JacksonXmlProperty(
            localName = "BizErr"
    )
    @Valid
    private BizErr bizErr;
}
