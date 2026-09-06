package com.dcep.supergw.dto.dc047;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 签约方列表
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:37:33
 */
@Data
public class CtrctPty implements Serializable {
    /**
     * 签约方意愿
     */
    @JacksonXmlProperty(
            localName = "CtrctPtyWill"
    )
    @Pattern(
            regexp = "SCPI01||SCPI02||SCPI03"
    )
    private String ctrctPtyWill;

    /**
     * 签约方钱包信息
     */
    @JacksonXmlProperty(
            localName = "WltInf"
    )
    @Valid
    private WltInf wltInf;

    /**
     * 商户信息
     */
    @JacksonXmlProperty(
            localName = "SndrMrchntInf"
    )
    @Valid
    private SndrMrchntInf sndrMrchntInf;

    /**
     * 签约方机构信息
     */
    @JacksonXmlProperty(
            localName = "CtrctPtyInf"
    )
    @Valid
    private CtrctPtyInf ctrctPtyInf;
}
