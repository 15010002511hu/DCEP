package com.dcep.supergw.dto.dc421;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 组件TransactionQueryDefinition
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:26:48
 */
@Data
public class TxQryDef implements Serializable {
    /**
     * 组件--TransactionCriteria
     */
    @JacksonXmlProperty(
            localName = "TxCrit"
    )
    @NotNull
    @Valid
    private TxCrit txCrit;
}
