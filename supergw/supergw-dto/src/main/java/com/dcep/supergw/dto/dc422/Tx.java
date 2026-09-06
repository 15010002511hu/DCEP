package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import lombok.Data;

/**
 * 组件--------Transaction
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:28:03
 */
@Data
public class Tx implements Serializable {
    /**
     * 组件----------PaymentFrom
     */
    @JacksonXmlProperty(
            localName = "PmtFr"
    )
    @Valid
    private PmtFr pmtFr;

    /**
     * 组件----------Payment
     */
    @JacksonXmlProperty(
            localName = "Pmt"
    )
    @Valid
    private Pmt pmt;
}
