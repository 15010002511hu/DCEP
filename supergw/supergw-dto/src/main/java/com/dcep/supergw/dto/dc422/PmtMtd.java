package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件------------PaymentMethod
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:27:56
 */
@Data
public class PmtMtd implements Serializable {
    /**
     * 原业务报文编号
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
