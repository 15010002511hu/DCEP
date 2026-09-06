package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--TransactionCriteria
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:59:28
 */
@Data
public class TxCrit implements Serializable {
    /**
     * 组件----NewCriteria
     */
    @JacksonXmlProperty(
            localName = "NewCrit"
    )
    @NotNull
    @Valid
    private NewCrit newCrit;
}
