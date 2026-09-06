package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件SupplementaryData
 * @Author luteng
 * @date 2025-10-12 16:16:50
 */
@Data
public class SplmtryData implements Serializable {
    /**
     * 组件--PlaceAndName
     */
    @JacksonXmlProperty(
            localName = "PlcAndNm"
    )
    @Length(
            min = 1,
            max = 350
    )
    @NotBlank
    private String plcAndNm;

    /**
     * 组件--Envelope
     */
    @JacksonXmlProperty(
            localName = "Envlp"
    )
    @NotNull
    @Valid
    private Envlp envlp;
}
