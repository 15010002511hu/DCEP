package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件----Identification
 * @Author luteng
 * @date 2025-10-12 16:17:05
 */
@Data
public class Id implements Serializable {
    /**
     * 组件------Other
     */
    @JacksonXmlProperty(
            localName = "Othr"
    )
    @NotNull
    @Valid
    private Othr othr;
}
