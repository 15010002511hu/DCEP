/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc381;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 商户信息
 * 
 * @author laimincai
 * @date 2023/12/29
 */
public class MrchntInf implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户属性
     */
    @JacksonXmlProperty(localName = "MrchntPrprty")
    @Pattern(regexp = "^[M][P][0-9]{2}")
    private String            mrchntPrprty;
    
    /**
     * 商户编码
     */
    @JacksonXmlProperty(localName = "MrchntNo")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            mrchntNo;

    /**
     * 商户简称
     */
    @JacksonXmlProperty(localName = "MrchntAbbrNm")
    @Length(min = 1, max = 30)
    private String            mrchntAbbrNm;

    /**
     * 商户名称
     */
    @JacksonXmlProperty(localName = "MrchntNm")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 200)
    private String            mrchntNm;
    
    /**
     * 商户类别代码
     */
    @JacksonXmlProperty(localName = "MCC")
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mcc;
    
    /**
     * 商户系统下单时生成的订单号，由商户自定义生成，要求商户订单号保持唯一性，该字段必填（本期互联互通平台不校验）
     * 受理订单号
     */
    @JacksonXmlProperty(localName = "OutOrdrNo")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            outOrdrNo;
    
    /**
     * 商户证件类型
     */
    @JacksonXmlProperty(localName = "MrchntIdTp")
    @Length(min = 4, max = 4)
    @Pattern(regexp = "^[I][T][0-9]{2}")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            mrchntIdTp;

    /**
     * 商户证件编码
     */
    @JacksonXmlProperty(localName = "MrchntIdNo")
    @Length(min = 1, max = 32)
    private String            mrchntIdNo;
    
    /**
     * 商品名称
     */
    @JacksonXmlProperty(localName = "GdNm")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 200)
    private String            gdNm;
    
    /**
     * 商品详情
     */
    @JacksonXmlProperty(localName = "GdDtls")
    @Length(min = 1, max = 1024)
    private String            gdDtls;

    /**
     * 订单号
     */
    @JacksonXmlProperty(localName = "OrdrNo")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 64)
    private String            ordrNo;

    /**
     * 订单时间
     */
    @JacksonXmlProperty(localName = "OrdrTm")
    @NotBlank(groups = Priority.Highest.class)
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
    private String            ordrTm;

    /**
     * 订单详情
     */
    @JacksonXmlProperty(localName = "OrdrDtls")
    @Length(min = 1, max = 4096)
    private String            ordrDtls;
    
    
    /**
     * 订单有效期
     */
    @JacksonXmlProperty(localName = "OrdrExp")
    @Pattern(regexp = "^([3-9]\\d|1[0-7]\\d|180)$", message = "订单有效期有效时间范围为30~180")
    private String            ordrExp;


    public String getMrchntPrprty() {
        return mrchntPrprty;
    }


    public void setMrchntPrprty(String mrchntPrprty) {
        this.mrchntPrprty = mrchntPrprty;
    }


    public String getMrchntNo() {
        return mrchntNo;
    }


    public void setMrchntNo(String mrchntNo) {
        this.mrchntNo = mrchntNo;
    }


    public String getMrchntAbbrNm() {
        return mrchntAbbrNm;
    }


    public void setMrchntAbbrNm(String mrchntAbbrNm) {
        this.mrchntAbbrNm = mrchntAbbrNm;
    }


    public String getMrchntNm() {
        return mrchntNm;
    }


    public void setMrchntNm(String mrchntNm) {
        this.mrchntNm = mrchntNm;
    }


    public String getMcc() {
        return mcc;
    }


    public void setMcc(String mcc) {
        this.mcc = mcc;
    }


    public String getOutOrdrNo() {
        return outOrdrNo;
    }


    public void setOutOrdrNo(String outOrdrNo) {
        this.outOrdrNo = outOrdrNo;
    }


    public String getMrchntIdTp() {
        return mrchntIdTp;
    }


    public void setMrchntIdTp(String mrchntIdTp) {
        this.mrchntIdTp = mrchntIdTp;
    }


    public String getMrchntIdNo() {
        return mrchntIdNo;
    }


    public void setMrchntIdNo(String mrchntIdNo) {
        this.mrchntIdNo = mrchntIdNo;
    }


    public String getGdNm() {
        return gdNm;
    }


    public void setGdNm(String gdNm) {
        this.gdNm = gdNm;
    }


    public String getGdDtls() {
        return gdDtls;
    }


    public void setGdDtls(String gdDtls) {
        this.gdDtls = gdDtls;
    }


    public String getOrdrNo() {
        return ordrNo;
    }


    public void setOrdrNo(String ordrNo) {
        this.ordrNo = ordrNo;
    }


    public String getOrdrTm() {
        return ordrTm;
    }


    public void setOrdrTm(String ordrTm) {
        this.ordrTm = ordrTm;
    }


    public String getOrdrDtls() {
        return ordrDtls;
    }


    public void setOrdrDtls(String ordrDtls) {
        this.ordrDtls = ordrDtls;
    }


    public String getOrdrExp() {
        return ordrExp;
    }


    public void setOrdrExp(String ordrExp) {
        this.ordrExp = ordrExp;
    }

    @Override
    public String toString() {
        return "MrchntInf [mrchntPrprty=" + mrchntPrprty + ", mrchntNo=" + mrchntNo + ", mrchntAbbrNm=" + mrchntAbbrNm
                + ", mrchntNm=" + mrchntNm + ", mcc=" + mcc + ", outOrdrNo=" + outOrdrNo + ", mrchntIdTp=" + mrchntIdTp
                + ", mrchntIdNo=" + mrchntIdNo + ", gdNm=" + gdNm + ", gdDtls=" + gdDtls + ", ordrNo=" + ordrNo
                + ", ordrTm=" + ordrTm + ", ordrDtls=" + ordrDtls + ", ordrExp=" + ordrExp + "]";
    }

}
