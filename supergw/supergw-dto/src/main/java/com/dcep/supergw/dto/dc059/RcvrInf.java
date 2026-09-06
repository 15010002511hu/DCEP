package com.dcep.supergw.dto.dc059;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import lombok.Data;

/**
 * 接收方信息
 *
 * @Author qinchaoyong
 * @date 2024-11-22 09:26:25
 */
@Data
public class RcvrInf implements Serializable {
    /**
     * 接收方客户
     */
    @JacksonXmlProperty(
            localName = "RcvrCstmr"
    )
    @Valid
    private RcvrCstmr rcvrCstmr;

    /**
     * 接收方预言机
     */
    @JacksonXmlProperty(
            localName = "RcvrOracle"
    )
    @Valid
    private RcvrOracle rcvrOracle;
}
