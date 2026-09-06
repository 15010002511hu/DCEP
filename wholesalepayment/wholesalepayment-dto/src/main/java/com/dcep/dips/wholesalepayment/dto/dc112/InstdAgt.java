package com.dcep.dips.wholesalepayment.dto.dc112;

import com.dcep.common.model.soap.FinInstnId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--InstructedAgent
 * @Author zhaotianwu
 * @date 2025-10-13 16:56:00
 */
@Data
public class InstdAgt implements Serializable {
    /**
     * 接收直接参与机构
     */
    @JacksonXmlProperty(
            localName = "FinInstnId"
    )
    @NotNull
    @Valid
    private FinInstnId finInstnId;

    /**
     * 组件----BranchIdentification
     */
    @JacksonXmlProperty(
            localName = "BrnchId"
    )
    @Valid
    private BrnchId brnchId;
}
