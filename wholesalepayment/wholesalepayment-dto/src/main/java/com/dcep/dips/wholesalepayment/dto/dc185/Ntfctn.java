package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件Notification
 * @Author luteng
 * @date 2025-10-12 16:17:10
 */
@Data
public class Ntfctn implements Serializable {
    /**
     * 唯一标识
     */
    @JacksonXmlProperty(
            localName = "Id"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    private String id;

    /**
     * 结算钱包信息
     */
    @JacksonXmlProperty(
            localName = "Acct"
    )
    @NotNull
    @Valid
    private Acct acct;

    /**
     * 组件--Entry
     */
    @JacksonXmlProperty(
            localName = "Ntry"
    )
    @NotNull
    @Valid
    private Ntry ntry;
}
