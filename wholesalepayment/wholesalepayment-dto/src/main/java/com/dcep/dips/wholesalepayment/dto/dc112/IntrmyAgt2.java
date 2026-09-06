package com.dcep.dips.wholesalepayment.dto.dc112;

import com.dcep.common.model.soap.FinInstnId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--IntermediaryAgent2
 * @Author zhaotianwu
 * @date 2025-10-13 16:55:58
 */
@Data
public class IntrmyAgt2 implements Serializable {
    /**
     * 中间参与机构2
     */
    @JacksonXmlProperty(
            localName = "FinInstnId"
    )
    @NotNull
    @Valid
    private FinInstnId finInstnId;
}
