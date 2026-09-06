package com.dcep.dips.wholesalepayment.dto.dc427;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件------Creator
 * @Author luteng
 * @date 2025-10-21 16:31:33
 */
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Cretr implements Serializable {
    /**
     * 原业务发起参与机构
     */
    @JacksonXmlProperty(
            localName = "Agt"
    )
    @NotNull
    @Valid
    private Agt agt;
}
