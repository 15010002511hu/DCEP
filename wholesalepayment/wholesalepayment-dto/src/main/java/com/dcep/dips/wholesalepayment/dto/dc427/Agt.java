package com.dcep.dips.wholesalepayment.dto.dc427;

import com.dcep.common.model.soap.FinInstnId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件----Agent
 * @Author luteng
 * @date 2025-10-21 16:31:39
 */
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Agt implements Serializable {
    /**
     * 参与机构组件
     */
    @JacksonXmlProperty(
            localName = "FinInstnId"
    )
    @NotNull
    @Valid
    private FinInstnId finInstnId;
}
