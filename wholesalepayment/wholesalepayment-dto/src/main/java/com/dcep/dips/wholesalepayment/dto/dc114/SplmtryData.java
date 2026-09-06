package com.dcep.dips.wholesalepayment.dto.dc114;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--SupplementaryData
 * @Author zhaotianwu
 * @date 2025-10-13 17:13:28
 */
@Data
public class SplmtryData implements Serializable {
    /**
     * 组件----PlaceAndName
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
     * 组件----Envelope
     */
    @JacksonXmlProperty(
            localName = "Envlp"
    )
    @NotNull
    @Valid
    private Envlp envlp;
}
