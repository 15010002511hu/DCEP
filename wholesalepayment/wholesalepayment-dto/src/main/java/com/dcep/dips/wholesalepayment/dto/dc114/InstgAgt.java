package com.dcep.dips.wholesalepayment.dto.dc114;

import com.dcep.common.model.soap.FinInstnId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 发起参与机构
 * @Author zhaotianwu
 * @date 2025-10-13 17:14:01
 */
@Data
public class InstgAgt implements Serializable {
    /**
     * 参与机构组件
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
