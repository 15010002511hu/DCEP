package com.dcep.supergw.dto.dc507;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author laimincai
 * @version : TxAuthrtyInf.java v 0.1 2023-01-05
 * @description : 交易权限信息
 */
@Getter
@Setter
@ToString
public class TxAuthrtyInf implements Serializable {

    private static final long serialVersionUID = -5429611846364788811L;

    /**
     * 交易权限编码
     */
    @JacksonXmlProperty(localName = "TxAuthrtyCd")
    @NotBlank
    @Pattern(regexp = "TCC01||TCC02||TCC03")
    private String txAuthrtyCd;

    /**
     * 交易权限开通标识
     */
    @JacksonXmlProperty(localName = "OpenFlg")
    @NotBlank
    @Pattern(regexp = "OF00||OF01||OA00||OA01")
    private String openFlg;
}
