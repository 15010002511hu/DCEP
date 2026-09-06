package com.dcep.supergw.dto.dc416;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 收款钱包信息
 * @author chenkai
 * 修改说明2021-09-26：商品名称字段长度由40改为200，by:weiqianjing
 */
@JacksonXmlRootElement(localName = "Cdtr")
@Getter
@Setter
@ToString
public class Cdtr implements Serializable{

    private static final long serialVersionUID = -5515841010881898747L;
    
    /**
     * 收款人名称
     */
    @JacksonXmlProperty(localName = "CdtrNm")
    @Length(min = 1,max = 60)
    private String                  cdtrNm;

    
    /**
     * 收款钱包名称
     */
    @JacksonXmlProperty(localName = "CdtrWltNm")
    @Length(min = 1,max = 60)
    @NotBlank
    private String                  cdtrWltNm;

    /**
     * 收款钱包ID
     */
    @JacksonXmlProperty(localName = "CdtrWltId")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min=1,max = 34)
    @NotBlank
    private String                  cdtrWltId;

    /**
     * 收款钱包类型
     * WT01：个人钱包
     * WT02：子个人钱包
     * WT09：对公钱包
     * WT10：子对公钱包
     */
    @JacksonXmlProperty(localName = "CdtrWltTp")
    @Length(min=4,max = 4)
    @Pattern(regexp = "WT01||WT02||WT09||WT10")
    private String                  cdtrWltTp;

    /**
     * 收款钱包等级
     * WL01：一类钱包
     * WL02：二类钱包
     * WL03：三类钱包
     * WL04：四类钱包
     */
    @JacksonXmlProperty(localName = "CdtrWltLvl")
    @Length(min=4,max = 4)
    @Pattern(regexp = "WL01||WL02||WL03||WL04||WL05")
    private String                  cdtrWltLvl;

    /**
     * 收款人手机号
     */
    @JacksonXmlProperty(localName = "CdtrMblPhNo")
    @Length(min = 1,max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  cdtrMblPhNo;
    
    /**
     * 收款人邮件地址
     */
    @JacksonXmlProperty(localName = "CdtrEmailAdr")
    @Length(min = 1,max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  cdtrEmailAdr;
    
    /**
     * 收款运营机构
     */
    @JacksonXmlProperty(localName = "CdtrPtyId")
    @Length(min = 1,max = 14)
    private String                  cdtrPtyId;

    /**
     * 商户号
     */
    @JacksonXmlProperty(localName = "MrchntNo")
    @Length(min = 1,max = 35)
    private String                  mrchntNo;

    /**
     * 商户简称
     */
    @JacksonXmlProperty(localName = "MrchntAbbrNm")
    @Length(min = 1,max = 30)
    private String                  mrchntAbbrNm;

    /**
     * 商户名称
     */
    @JacksonXmlProperty(localName = "MrchntNm")
    @Length(min = 1,max = 60)
    private String                  mrchntNm;

    /**
     * 商户类别代码
     */
    @JacksonXmlProperty(localName = "MCC")
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mcc;
    /**
     * 商户证件类型
     * IT01：居民身份证
     * IT02：军官证
     * IT03：护照
     */
    @JacksonXmlProperty(localName = "MrchntIdTp")
    @Length(min = 4,max = 4)
    @Pattern(regexp = "IT[0-9]{2}")
    private String                  mrchntIdTp;

    /**
     * 商户证件编码
     */
    @JacksonXmlProperty(localName = "MrchntIdNo")
    @Length(min = 1,max = 32)
    private String                  mrchntIdNo;


    @JacksonXmlProperty(localName = "SubMrchntNo")
    @Length(min = 1,max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  subMrchntNo;

    @JacksonXmlProperty(localName = "SubMrchntWltId")
    @Length(min = 1,max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  subMrchntWltId;


    @JacksonXmlProperty(localName = "SubMrchntNm")
    @Length(min = 1,max = 60)
    private String                  subMrchntNm;

    @JacksonXmlProperty(localName = "SubMrchntAbbrNm")
    @Length(min = 1,max = 30)
    private String                  subMrchntAbbrNm;

    @JacksonXmlProperty(localName = "SubMCC")
    @Length(min = 1,max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  subMcc;

    @JacksonXmlProperty(localName = "SubMrchntIdTp")
    @Pattern(regexp = "IT[0-9]{2}")
    private String                  subMrchntIdTp;

    @JacksonXmlProperty(localName = "SubMrchntIdNo")
    @Length(min = 1,max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  subMrchntIdNo;

    /**
     * 商品名称
     */
    @JacksonXmlProperty(localName = "GdNm")
//    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1,max = 200)
    private String                  gdNm;

    /**
     * 订单号
     */
    @JacksonXmlProperty(localName = "OrdrNo")
//    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1,max = 40)
    private String                  ordrNo;


    /**
     * 订单时间
     */
    @JacksonXmlProperty(localName = "OrdrTm")
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
//    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1)
    @Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})?", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
    private String ordrTm;

    /**
     * 订单详情
     * 修改说明2021-09-29：长度改为1024
     */
    @JacksonXmlProperty(localName = "OrdrDtls")
    @Length(min = 1,max = 1024)
    private String                  ordrDtls;

    /**
     * 订单支付有效时间
     */
    @JacksonXmlProperty(localName = "OrdrPmtValTm")
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    @Length(min = 1)
    @Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})?", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
    private String                  ordrPmtValTm;


    @JacksonXmlProperty(localName = "ResdtTp")
    @Pattern(regexp = "REST[0-9]{2}")
    private String                  resdtTp;

    @JacksonXmlProperty(localName = "ResdtCtryCd")
    @Length(min = 1,max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  resdtCtryCd;

    @JacksonXmlProperty(localName = "OrdrRiskLvl")
    @Length(min = 1,max = 10)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  ordrRiskLvl;

}
