package com.dcep.dips.wholesalepayment.dto.dc192;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--StatusReasonInformation
 * @Author qiaopengyu
 * @date 2025-10-31 11:24:57
 */
@Data
public class StsRsnInf implements Serializable {
    /**
     * 组件----Reason
     */
    @JacksonXmlProperty(
            localName = "Rsn"
    )
    @NotNull
    @Valid
    private Rsn rsn;

    /**
     * 业务处理信息
     */
    @JacksonXmlProperty(
            localName = "AddtlInf"
    )
    @Length(
            min = 1,
            max = 105
    )
    @NotBlank
    private String addtlInf;
}
