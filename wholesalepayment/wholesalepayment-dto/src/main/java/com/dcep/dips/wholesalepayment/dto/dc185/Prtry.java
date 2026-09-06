package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件------Proprietary
 * @Author luteng
 * @date 2025-10-12 16:16:55
 */
@Data
public class Prtry implements Serializable {
    /**
     * 组件--------Code
     */
    @JacksonXmlProperty(
            localName = "Cd"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    private String cd;
}
