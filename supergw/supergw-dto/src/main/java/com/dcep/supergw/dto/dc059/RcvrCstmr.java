package com.dcep.supergw.dto.dc059;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 接收方客户
 *
 * @Author qinchaoyong
 * @date 2024-11-22 09:26:23
 */
@Data
public class RcvrCstmr implements Serializable {
    /**
     * 接收客户运营机构
     */
    @JacksonXmlProperty(
            localName = "RcvrPty"
    )
    @Length(
            min = 1,
            max = 14
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String rcvrPty;

    /**
     * 接收客户名称
     */
    @JacksonXmlProperty(
            localName = "RcvrCstmrNm"
    )
    @Length(
            min = 1,
            max = 240
    )
    private String rcvrCstmrNm;

    /**
     * 钱包ID
     */
    @JacksonXmlProperty(
            localName = "RcvrWltId"
    )
    @Length(
            min = 1,
            max = 68
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String rcvrWltId;

    /**
     * 钱包等级
     */
    @JacksonXmlProperty(
            localName = "RcvrWltLvl"
    )
    @Pattern(
            regexp = "WL01||WL02||WL03||WL04"
    )
    private String rcvrWltLvl;

    /**
     * 钱包类型
     */
    @JacksonXmlProperty(
            localName = "RcvrWltTp"
    )
    @Pattern(regexp = "WT[0-9]{2}")
    private String rcvrWltTp;

    /**
     * 钱包名称
     */
    @JacksonXmlProperty(
            localName = "RcvrWltNm"
    )
    @Length(
            min = 1,
            max = 60
    )
    private String rcvrWltNm;

    /**
     * 接收方合约实例
     */
    @JacksonXmlProperty(
            localName = "RcvrCtrctInst"
    )
    @NotNull
    @Valid
    private RcvrCtrctInst rcvrCtrctInst;
}
