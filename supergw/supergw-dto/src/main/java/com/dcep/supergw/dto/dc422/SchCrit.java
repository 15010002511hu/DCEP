package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件------SearchCriteria
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:59:26
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
