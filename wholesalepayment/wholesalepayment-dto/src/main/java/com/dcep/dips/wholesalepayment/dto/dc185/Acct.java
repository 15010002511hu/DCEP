package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 结算钱包信息
 * @Author luteng
 * @date 2025-10-12 16:17:07
 */
@Data
public class Acct implements Serializable {
    /**
     * 组件----Identification
     */
    @JacksonXmlProperty(
            localName = "Id"
    )
    @NotNull
    @Valid
    private Id id;

    /**
     * 结算钱包名称
     */
    @JacksonXmlProperty(
            localName = "Nm"
    )
    @Length(
            min = 1,
            max = 60
    )
    private String nm;

    /**
     * 参与机构信息
     */
    @JacksonXmlProperty(
            localName = "Ownr"
    )
    @Valid
    private Ownr ownr;
}
