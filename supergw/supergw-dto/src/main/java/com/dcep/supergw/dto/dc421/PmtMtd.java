package com.dcep.supergw.dto.dc421;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 组件--------------PaymentMethod
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:26:38
 */
@Data
public class PmtMtd implements Serializable {
    /**
     * 原报文编号
     */
    @JacksonXmlProperty(
            localName = "XMLMsgNm"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    private String xMLMsgNm;
}
