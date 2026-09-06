package com.dcep.dips.wholesalepayment.dto.dc200;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--SupplementaryData
 * @Author luteng
 * @date 2025-09-28 11:40:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
