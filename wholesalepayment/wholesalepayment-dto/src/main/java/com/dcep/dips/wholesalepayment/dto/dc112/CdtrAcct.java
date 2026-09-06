package com.dcep.dips.wholesalepayment.dto.dc112;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--CreditorAccount
 * @Author zhaotianwu
 * @date 2025-10-13 16:55:48
 */
@Data
public class CdtrAcct implements Serializable {
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
     * 收款方钱包名称
     */
    @JacksonXmlProperty(
            localName = "Nm"
    )
    @Length(
            min = 1,
            max = 70
    )
    @NotBlank
    private String nm;
}
