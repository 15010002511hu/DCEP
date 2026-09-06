package com.dcep.dips.wholesalepayment.dto.dc200;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件----Reason
 * @Author luteng
 * @date 2025-09-28 11:40:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rsn implements Serializable {
    /**
     * 业务处理码
     */
    @JacksonXmlProperty(
            localName = "Prtry"
    )
    @NotBlank
    private String prtry;
}
