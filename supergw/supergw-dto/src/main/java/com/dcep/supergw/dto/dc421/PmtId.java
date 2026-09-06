package com.dcep.supergw.dto.dc421;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 组件----------PaymentIdentification
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:26:43
 */
@Data
public class PmtId implements Serializable {
    /**
     * 组件------------LongBusinessIdentification
     */
    @JacksonXmlProperty(
            localName = "LngBizId"
    )
    @NotNull
    @Valid
    private LngBizId lngBizId;
}
