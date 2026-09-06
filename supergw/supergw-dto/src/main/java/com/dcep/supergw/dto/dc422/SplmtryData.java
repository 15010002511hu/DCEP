package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 组件SupplementaryData
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:27:54
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
