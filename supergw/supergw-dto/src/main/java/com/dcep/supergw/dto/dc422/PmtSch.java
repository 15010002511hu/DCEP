package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--------PaymentSearch
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:59:25
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
