package com.dcep.dips.wholesalepayment.dto.dc428;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件----CancellationStatusReasonInformation
 * @Author luteng
 * @date 2025-10-21 17:29:41
 */
@Data
@AllArgsConstructor
public class CxlStsRsnInf implements Serializable {
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
     * 原业务处理信息
     */
    @JacksonXmlProperty(
            localName = "AddtlInf"
    )
    @Length(
            min = 1,
            max = 105
    )
    private String addtlInf;
}
