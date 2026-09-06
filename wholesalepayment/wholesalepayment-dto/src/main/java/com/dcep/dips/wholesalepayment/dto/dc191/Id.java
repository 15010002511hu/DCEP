package com.dcep.dips.wholesalepayment.dto.dc191;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件----Identification
 * @Author qiaopengyu
 * @date 2025-10-30 21:17:00
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
