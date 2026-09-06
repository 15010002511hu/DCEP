package com.dcep.dips.wholesalepayment.dto.dc112;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 组件--RemittanceInformation
 * @Author zhaotianwu
 * @date 2025-10-13 16:55:45
 */
@Data
public class RmtInf implements Serializable {
    /**
     * 附言
     */
    @JacksonXmlElementWrapper(
            useWrapping = false
    )
    @JacksonXmlProperty(
            localName = "Ustrd"
    )
    private List<String> ustrd;
}
