package com.dcep.supergw.dto.dc052;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 剩余资金去向列表
 *
 * @Author qinchaoyong
 * @date 2024-11-15 16:31:54
 */
@Data
public class RmngFnd implements Serializable {
    /**
     * 客户名称
     */
    @JacksonXmlProperty(
            localName = "CstmrNm"
    )
    @Length(
            min = 1,
            max = 240
    )
    @NotBlank
    private String cstmrNm;

    /**
     * 钱包ID
     */
    @JacksonXmlProperty(
            localName = "WltId"
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
    private String wltId;

    /**
     * 钱包类型
     */
    @JacksonXmlProperty(
            localName = "WltTp"
    )
    @NotBlank
    @Pattern(regexp = "WT[0-9]{2}")
    private String wltTp;

    /**
     * 钱包等级
     */
    @JacksonXmlProperty(
            localName = "WltLvl"
    )
    @NotBlank
    @Pattern(
            regexp = "WL01||WL02||WL03||WL04"
    )
    private String wltLvl;

    /**
     * 金额
     */
    @JacksonXmlProperty(
            localName = "TxAmt"
    )
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount txAmt;

    /**
     * 钱包所属运营机构
     */
    @JacksonXmlProperty(
            localName = "WltPty"
    )
    @Length(
            min = 1,
            max = 14
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    @NotBlank
    private String wltPty;
}
