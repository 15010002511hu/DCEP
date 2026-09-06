package com.dcep.dips.wholesalepayment.dto.dc191;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 付款方
 * @Author qiaopengyu
 * @date 2025-10-30 21:17:03
 */
@Data
public class Dbtr implements Serializable {
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
