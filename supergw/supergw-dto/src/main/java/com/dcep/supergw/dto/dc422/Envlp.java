package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import lombok.Data;

/**
 * 组件--Envelope
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:27:53
 */
@Data
public class Envlp implements Serializable {
    /**
     * 合约应答
     */
    @JacksonXmlElementWrapper(
            useWrapping = false
    )
    @JacksonXmlProperty(
            localName = "CtrctRspsn"
    )
    @Valid
    private List<CtrctRspsn> ctrctRspsn;
}
