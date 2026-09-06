package com.dcep.supergw.dto.dc301;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : CdtrInf.java v 0.1 2021-04-13
 * @description :
 */
public class CdtrInf implements Serializable {

    /**
     * 商户所属运营机构
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 14)
    @JacksonXmlProperty(localName = "CdtrInstnId")
    private String cdtrInstnId;

    /**
     * 商户钱包ID
     */
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 34)
    @JacksonXmlProperty(localName = "MrchntWltId")
    private String mrchntWltId;

    /**
     * 商户属性
     */
    @Pattern(regexp = "MP[0-9]{2}")
    @JacksonXmlProperty(localName = "MrchntPrprty")
    private String mrchntPrprty;

    /**
     * 商户编码
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 35)
    @JacksonXmlProperty(localName = "MrchntNo")
    private String mrchntNo;

    /**
     * 商户名称
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 60)
    @JacksonXmlProperty(localName = "MrchntNm")
    private String mrchntNm;

    /**
     * 商户简称
     */
    @Length(min = 1, max = 30)
    @JacksonXmlProperty(localName = "MrchntAbbrNm")
    private String mrchntAbbrNm;

    /**
     * 商户类别代码
     */
    @JacksonXmlProperty(localName = "MCC")
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mcc;

    /**
     * 商户证件类型
     */
    @JacksonXmlProperty(localName = "MrchntIdTp")
    @Length(min = 1, max = 4)
    @Pattern(regexp = "IT[0-9]{2}")
    private String mrchntIdTp;

    /**
     * 商户证件编码
     */
    @JacksonXmlProperty(localName = "MrchntIdNo")
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mrchntIdNo;

    /**
     * 受理终端编号
     */
    @NotBlank
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "TerNo")
    private String terNo;

    /**
     * 受理终端IP
     */
    @NotBlank
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "TerIP")
    private String terIP;

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getCdtrInstnId() {
        return cdtrInstnId;
    }

    public void setCdtrInstnId(String cdtrInstnId) {
        this.cdtrInstnId = cdtrInstnId;
    }

    public String getMrchntWltId() {
        return mrchntWltId;
    }

    public void setMrchntWltId(String mrchntWltId) {
        this.mrchntWltId = mrchntWltId;
    }

    public String getMrchntNo() {
        return mrchntNo;
    }

    public void setMrchntNo(String mrchntNo) {
        this.mrchntNo = mrchntNo;
    }

    public String getMrchntNm() {
        return mrchntNm;
    }

    public void setMrchntNm(String mrchntNm) {
        this.mrchntNm = mrchntNm;
    }

    public String getMrchntAbbrNm() {
        return mrchntAbbrNm;
    }

    public void setMrchntAbbrNm(String mrchntAbbrNm) {
        this.mrchntAbbrNm = mrchntAbbrNm;
    }

    public String getTerNo() {
        return terNo;
    }

    public void setTerNo(String terNo) {
        this.terNo = terNo;
    }

    public String getTerIP() {
        return terIP;
    }

    public void setTerIP(String terIP) {
        this.terIP = terIP;
    }

    public String getMrchntPrprty() {
        return mrchntPrprty;
    }

    public void setMrchntPrprty(String mrchntPrprty) {
        this.mrchntPrprty = mrchntPrprty;
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

    @Override
    public String toString() {
        return "CdtrInf{" +
                "cdtrInstnId='" + cdtrInstnId + '\'' +
                ", mrchntWltId='" + mrchntWltId + '\'' +
                ", mrchntPrprty='" + mrchntPrprty + '\'' +
                ", mrchntNo='" + mrchntNo + '\'' +
                ", mrchntNm='" + mrchntNm + '\'' +
                ", mrchntAbbrNm='" + mrchntAbbrNm + '\'' +
                ", mcc='" + mcc + '\'' +
                ", mrchntIdTp='" + mrchntIdTp + '\'' +
                ", mrchntIdNo='" + mrchntIdNo + '\'' +
                ", terNo='" + terNo + '\'' +
                ", terIP='" + terIP + '\'' +
                '}';
    }
}
