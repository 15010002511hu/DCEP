package com.dcep.dips.wholesalepayment.dto.dc191;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 代理1组件
 * @Author liuyuzeng
 * @date 2025-10-27 21:13:37
 */
@Data
public class PrvsInstgAgt1 implements Serializable {
    private static final long serialVersionUID = -8224054556964814778L;
    /**
     * 参与机构组件
     */
    @JacksonXmlProperty(
            localName = "FinInstnId"
    )
    @NotNull
    private FinInstnId finInstnId;
}
