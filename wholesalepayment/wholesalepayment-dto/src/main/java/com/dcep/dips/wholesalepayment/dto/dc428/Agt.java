package com.dcep.dips.wholesalepayment.dto.dc428;

import com.dcep.common.model.soap.FinInstnId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 发起参与机构
 * @Author luteng
 * @date 2025-10-21 17:29:47
 */
@Data
@AllArgsConstructor
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
