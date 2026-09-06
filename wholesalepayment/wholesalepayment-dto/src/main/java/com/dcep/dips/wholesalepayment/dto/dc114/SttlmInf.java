package com.dcep.dips.wholesalepayment.dto.dc114;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件--SettlementInformation
 * @Author zhaotianwu
 * @date 2025-10-13 17:14:15
 */
@Data
public class SttlmInf implements Serializable {
    /**
     * 组件----SettlementMethod
     */
    @JacksonXmlProperty(
            localName = "SttlmMtd"
    )
    @Length(
            min = 1,
            max = 4
    )
    @NotBlank
    private String sttlmMtd;
}
