package com.dcep.dips.wholesalepayment.dto.dc114;

import com.dcep.common.model.soap.FinInstnId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件------Agent
 * @Author zhaotianwu
 * @date 2025-10-13 17:13:47
 */
@Data
public class Agt implements Serializable {
    /**
     * 付款直接参与机构
     */
    @JacksonXmlProperty(
            localName = "FinInstnId"
    )
    @NotNull
    @Valid
    private FinInstnId finInstnId;
}
