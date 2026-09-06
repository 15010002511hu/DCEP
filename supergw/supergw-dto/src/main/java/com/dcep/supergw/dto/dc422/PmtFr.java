package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import lombok.Data;

/**
 * 组件----------PaymentFrom
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:28:01
 */
@Data
public class PmtFr implements Serializable {
    /**
     * 组件------------MemberIdentification
     */
    @JacksonXmlProperty(
            localName = "MmbId"
    )
    @Valid
    private MmbId mmbId;
}
