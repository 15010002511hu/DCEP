package com.dcep.supergw.dto.dc035;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import lombok.Data;

/**
 * 签约方列表
 *
 * @Author qinchaoyong
 * @date 2024-09-11 09:55:20
 */
@Data
public class SgntrPty implements Serializable {
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
