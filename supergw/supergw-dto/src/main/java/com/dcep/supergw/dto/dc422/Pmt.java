package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 组件----------Payment
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:27:57
 */
@Data
public class Pmt implements Serializable {
    /**
     * 原报文标识号
     */
    @JacksonXmlProperty(
            localName = "MsgId"
    )
    @Length(
            min = 1,
            max = 35
    )
    private String msgId;

    /**
     * 组件------------PaymentMethod
     */
    @JacksonXmlProperty(
            localName = "PmtMtd"
    )
    @NotNull
    @Valid
    private PmtMtd pmtMtd;
}
