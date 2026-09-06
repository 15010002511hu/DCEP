package com.dcep.dips.wholesalepayment.dto.dc112;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件------PostalAddress
 * @Author zhaotianwu
 * @date 2025-10-13 16:55:55
 */
@Data
public class PstlAdr implements Serializable {
    /**
     * 付款方所在国家
     */
    @JacksonXmlProperty(
            localName = "Ctry"
    )
    @NotBlank
    private String ctry;
}
