package com.dcep.dips.wholesalepayment.dto.dc112;

import com.dcep.common.model.soap.FinInstnId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--IntermediaryAgent1
 * @Author zhaotianwu
 * @date 2025-10-13 16:55:58
 */
@Data
public class IntrmyAgt1 implements Serializable {
    /**
     * 中间参与机构1
     */
    @JacksonXmlProperty(
            localName = "FinInstnId"
    )
    @NotNull
    @Valid
    private FinInstnId finInstnId;
}
