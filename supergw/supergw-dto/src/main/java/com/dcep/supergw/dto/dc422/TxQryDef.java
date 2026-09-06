package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件TransactionQueryDefinition
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:59:29
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
