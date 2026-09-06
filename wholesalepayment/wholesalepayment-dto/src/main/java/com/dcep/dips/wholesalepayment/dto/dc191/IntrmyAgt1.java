package com.dcep.dips.wholesalepayment.dto.dc191;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--IntermediaryAgent1
 * @Author qiaopengyu
 * @date 2025-10-30 21:17:03
 */
@Data
public class IntrmyAgt1 implements Serializable {
    private static final long serialVersionUID = 7851618841564300662L;
    /**
     * 中间行1
     */
    @JacksonXmlProperty(
            localName = "FinInstnId"
    )
    @NotNull
    private FinInstnId finInstnId;
}
