package com.dcep.supergw.dto.dc036;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 签约方信息验证结果
 *
 * @Author qinchaoyong
 * @date 2024-09-11 09:57:56
 */
@Data
public class ChckRspnInf implements Serializable {
    /**
     * 验证结果
     */
    @JacksonXmlProperty(
            localName = "ChckRslt"
    )
    @Length(
            min = 1,
            max = 5
    )
    @NotBlank
    @Pattern(
            regexp = "YES||NO"
    )
    private String chckRslt;

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
