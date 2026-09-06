package com.dcep.dips.wholesalepayment.dto.dc427;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 发起参与机构
 * @Author luteng
 * @date 2025-10-21 16:31:40
 */
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Assgnr implements Serializable {
    /**
     * 组件----Agent
     */
    @JacksonXmlProperty(
            localName = "Agt"
    )
    @NotNull
    @Valid
    private Agt agt;
}
