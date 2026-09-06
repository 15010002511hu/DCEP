package com.dcep.supergw.dto.dc507;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * ProtocolInformation 协议信息
 * 
 * @author duz
 */
@JacksonXmlRootElement(localName = "PtcInf")
@Getter
@Setter
@ToString
public class PtcInf2 implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -5209292812749708108L;

    /**
     * 签约协议号
     */
    @JacksonXmlProperty(localName = "PtcId")
    @Length(min = 1, max = 34)
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String ptcId;

    /**
     * 商户侧账号
     */
    @JacksonXmlProperty(localName = "MrchntAcctId")
    @Length(min = 1, max = 256)
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mrchntAcctId;

    /**
     * 验证类型
     */
    @JacksonXmlProperty(localName = "VrfyTp")
    @Length(min = 1, max = 4)
    @NotBlank
    @Pattern(regexp = "VT01||VT02||VT03||VT04||VT05||VT06||VT07")
    private String vrfyTp;

    /**
     * 授权码
     */
    @JacksonXmlProperty(localName = "VrfyCd")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(max = 128)
    private String vrfyCd;

    /**
     * 钱包ID辨识码
     */
    @JacksonXmlProperty(localName = "WltShrtId")
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 4)
    private String wltShrtId;

    /**
     * 交易权限列表
     */
    @JacksonXmlElementWrapper(localName = "TxAuthrtyList")
    @JacksonXmlProperty(localName = "TxAuthrtyInf")
    @Valid
    private List<TxAuthrtyInf> txAuthrtyInfList;

    /**
     * 用户签署商户协议信息
     */
    @JacksonXmlElementWrapper(localName = "MrchntPtcList")
    @JacksonXmlProperty(localName = "MrchntPtcInf")
    @Valid
    private List<MrchntPtcInf> mrchntPtcInfList;

    /**
     * 钱包手机号
     */
    @JacksonXmlProperty(localName = "MblPhNo")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 70)
    private String mobilePhoneNumber;

    /**
     * 钱包开立时IP地址
     */
    @JacksonXmlProperty(localName = "IPAdr")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 64)
    private String ipAddress;

    /**
     * 钱包等级
     */
    @JacksonXmlProperty(localName = "WltLvl")
    @Length(min = 4, max = 4)
    @Pattern(regexp = "WL01||WL02||WL03||WL04")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String wltLvl;

    /**
     * 是否境外钱包
     */
    @JacksonXmlProperty(localName = "IsOvrSeaWlt")
    @Pattern(regexp = "false||true")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String isOvrSeaWlt;

    /**
     * 钱包ID
     */
    @JacksonXmlProperty(localName = "WltId")
    @Length(min = 16, max = 16)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String wltId;

    /**
     * 单笔交易金额上限
     */
    @JacksonXmlProperty(localName = "WltSnglTxAmtLmt")
    @Valid
    private ActiveCurrencyAndAmount wltSnglTxAmtLmt;

    /**
     * 当日交易金额上限
     */
    @JacksonXmlProperty(localName = "WltDayTtlAmtLmt")
    @Valid
    private ActiveCurrencyAndAmount wltDayTtlAmtLmt;

    /**
     * 国家和地区代码
     */
    @JacksonXmlProperty(localName = "CtryAndRgnlCd")
    @Length(min = 2, max = 2)
    private String ctryAndRgnlCd;

}
