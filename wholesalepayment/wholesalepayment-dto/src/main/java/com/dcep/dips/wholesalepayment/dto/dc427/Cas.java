package com.dcep.dips.wholesalepayment.dto.dc427;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件----Case
 * @Author luteng
 * @date 2025-10-21 16:31:34
 */
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Cas implements Serializable {
    /**
     * 原报文标识号
     */
    @JacksonXmlProperty(
            localName = "Id"
    )
    @Length(
            min = 32,
            max = 32
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String id;

    /**
     * 组件------Creator
     */
    @JacksonXmlProperty(
            localName = "Cretr"
    )
    @NotNull
    @Valid
    private Cretr cretr;
}
