package com.dcep.supergw.dto.dc059;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import lombok.Data;

/**
 * 发起方信息
 *
 * @Author qinchaoyong
 * @date 2024-11-22 09:26:31
 */
@Data
public class SndrInf implements Serializable {
    /**
     * 发起方客户
     */
    @JacksonXmlProperty(
            localName = "SndrCstmr"
    )
    @Valid
    private SndrCstmr sndrCstmr;

    /**
     * 发起方合约实例
     */
    @JacksonXmlElementWrapper(
            useWrapping = false
    )
    @JacksonXmlProperty(
            localName = "SndrCtrctInst"
    )
    @Valid
    private List<SndrCtrctInst> sndrCtrctInst;

    /**
     * 发起方预言机
     */
    @JacksonXmlProperty(
            localName = "SndrOracle"
    )
    @Valid
    private SndrOracle sndrOracle;

    /**
     * 发起方商户信息
     */
    @JacksonXmlProperty(
            localName = "SndrMrchntInf"
    )
    @Valid
    private SndrMrchntInf sndrMrchntInf;

    /**
     * 发起方机构信息
     */
    @JacksonXmlProperty(
            localName = "SndrPtyInf"
    )
    @Valid
    private SndrPtyInf sndrPtyInf;
}
