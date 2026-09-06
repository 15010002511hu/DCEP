package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 组件----PaymentCommonInformation
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:28:11
 */
@Data
public class PmtCmonInf implements Serializable {
    /**
     * 组件------CommonStatus
     */
    @JacksonXmlProperty(
            localName = "CmonSts"
    )
    @NotNull
    @Valid
    private CmonSts cmonSts;

    /**
     * 组件------EntryDate
     */
    @JacksonXmlProperty(
            localName = "NtryDt"
    )
    @Valid
    private NtryDt ntryDt;
}
