package com.dcep.dips.wholesalepayment.dto.dc428;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件----ResolvedCase
 * @Author luteng
 * @date 2025-10-21 17:29:42
 */
@Data
@AllArgsConstructor
public class RslvdCase implements Serializable {
    /**
     * 撤销申请报文标识号
     */
    @JacksonXmlProperty(
            localName = "Id"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String id;

    /**
     * 发起参与机构
     */
    @JacksonXmlProperty(
            localName = "Cretr"
    )
    @NotNull
    @Valid
    private Cretr cretr;
}
