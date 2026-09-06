package com.dcep.dips.wholesalepayment.dto.dc427;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件----OriginalGroupInformation
 * @Author luteng
 * @date 2025-10-21 16:31:32
 */
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrgnlGrpInf implements Serializable {
    /**
     * 原报文标识号
     */
    @JacksonXmlProperty(
            localName = "OrgnlMsgId"
    )
    @Length(
            min = 32,
            max = 32
    )
    @NotBlank
    private String orgnlMsgId;

    /**
     * 原报文编号
     */
    @JacksonXmlProperty(
            localName = "OrgnlMsgNmId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    private String orgnlMsgNmId;
}
