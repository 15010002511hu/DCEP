package com.dcep.dips.wholesalepayment.dto.dc427;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件----CancellationReasonInformation
 * @Author luteng
 * @date 2025-10-21 16:31:32
 */
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CxlRsnInf implements Serializable {
    /**
     * 组件------Reason
     */
    @JacksonXmlProperty(
            localName = "Rsn"
    )
    @NotNull
    @Valid
    private Rsn rsn;

    /**
     * 业务撤销处理信息
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
