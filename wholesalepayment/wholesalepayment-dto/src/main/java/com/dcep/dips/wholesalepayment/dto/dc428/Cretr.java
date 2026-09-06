package com.dcep.dips.wholesalepayment.dto.dc428;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 发起参与机构
 * @Author luteng
 * @date 2025-10-21 17:29:41
 */
@Data
@AllArgsConstructor
public class Cretr implements Serializable {
    /**
     * 组件--------Agent
     */
    @JacksonXmlProperty(
            localName = "Agt"
    )
    @NotNull
    @Valid
    private Agt agt;
}
