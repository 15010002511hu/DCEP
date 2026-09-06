package com.dcep.dips.wholesalepayment.dto.mcbs101;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件--StatusReasonInformation
 * @Author luzhikuan
 * @date 2025-10-03 15:53:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StsRsnInf implements Serializable {

    /**
     * 失败原因
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
