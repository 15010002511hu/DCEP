package com.dcep.supergw.dto.dc421;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 组件--TransactionCriteria
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:26:47
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
