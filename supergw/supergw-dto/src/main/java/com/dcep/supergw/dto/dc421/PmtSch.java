package com.dcep.supergw.dto.dc421;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 组件--------PaymentSearch
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:26:44
 */
@Data
public class PmtSch implements Serializable {
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
    @NotBlank
    private String msgId;

    /**
     * 组件----------PaymentIdentification
     */
    @JacksonXmlProperty(
            localName = "PmtId"
    )
    @NotNull
    @Valid
    private PmtId pmtId;
}
