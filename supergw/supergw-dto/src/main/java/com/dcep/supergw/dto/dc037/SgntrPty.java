package com.dcep.supergw.dto.dc037;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 签约方列表
 *
 * @Author qinchaoyong
 * @date 2024-11-26 14:16:09
 */
@Data
public class SgntrPty implements Serializable {
    /**
     * 签约方意愿
     */
    @JacksonXmlProperty(
            localName = "SgntrIntntn"
    )
    @Pattern(
            regexp = "SCPI01||SCPI02||SCPI03"
    )
    private String sgntrIntntn;

    /**
     * 签约方钱包信息
     */
    @JacksonXmlProperty(
            localName = "SgntrWltInf"
    )
    @Valid
    private SgntrWltInf sgntrWltInf;

    /**
     * 签约方商户信息
     */
    @JacksonXmlProperty(
            localName = "SgntrMrchntInf"
    )
    @Valid
    private SgntrMrchntInf sgntrMrchntInf;

    /**
     * 签约方机构信息
     */
    @JacksonXmlProperty(
            localName = "SgntrInstnInf"
    )
    @Valid
    private SgntrInstnInf sgntrInstnInf;
}
