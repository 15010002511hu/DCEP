package com.dcep.supergw.dto.dc421;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 组件------SearchCriteria
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:26:45
 */
@Data
public class SchCrit implements Serializable {
    /**
     * 组件--------PaymentSearch
     */
    @JacksonXmlProperty(
            localName = "PmtSch"
    )
    @NotNull
    @Valid
    private PmtSch pmtSch;
}
