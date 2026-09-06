package com.dcep.dips.wholesalepayment.dto.dc200;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--StatusReasonInformation
 * @Author luteng
 * @date 2025-09-28 11:40:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
