package com.dcep.dips.wholesalepayment.dto.dc114;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--ReturnReasonInformation
 * @Author zhaotianwu
 * @date 2025-10-13 17:13:39
 */
@Data
public class RtrRsnInf implements Serializable {
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
     * 退汇原因说明
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
