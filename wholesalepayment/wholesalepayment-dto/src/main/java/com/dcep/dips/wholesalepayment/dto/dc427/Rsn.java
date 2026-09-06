package com.dcep.dips.wholesalepayment.dto.dc427;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件------Reason
 * @Author luteng
 * @date 2025-10-21 16:31:31
 */
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Rsn implements Serializable {
    /**
     * 业务撤销处理码
     */
    @JacksonXmlProperty(
            localName = "Prtry"
    )
    @NotBlank
    private String prtry;
}
