package com.dcep.supergw.dto.dc301;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class SubMrchntInf implements Serializable {

    /**
     * 二级商户编码
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SubMrchntNo")
    private String subMrchntNo;

    /**
     * 二级商户钱包ID
     */
    @Length(min = 1, max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SubMrchntWltId")
    private String subMrchntWltId;

    /**
     * 二级商户名称
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 60)
    @JacksonXmlProperty(localName = "SubMrchntNm")
    private String subMrchntNm;

    /**
     * 二级商户简称
     */
    @Length(min = 1, max = 30)
    @JacksonXmlProperty(localName = "SubMrchntAbbrNm")
    private String subMrchntAbbrNm;


    /**
     * 二级商户类别代码
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SubMCC")
    private String subMCC;

    /**
     * 二级商户证件类型
     */
    @Length(min = 1, max = 4)
    @Pattern(regexp = "IT[0-9]{2}")
    @JacksonXmlProperty(localName = "SubMrchntIdTp")
    private String subMrchntIdTp;

    /**
     * 二级商户证件编码
     */
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SubMrchntIdNo")
    private String subMrchntIdNo;

    public String getSubMrchntNo() {
        return subMrchntNo;
    }

    public void setSubMrchntNo(String subMrchntNo) {
        this.subMrchntNo = subMrchntNo;
    }

    public String getSubMrchntWltId() {
        return subMrchntWltId;
    }

    public void setSubMrchntWltId(String subMrchntWltId) {
        this.subMrchntWltId = subMrchntWltId;
    }

    public String getSubMrchntNm() {
        return subMrchntNm;
    }

    public void setSubMrchntNm(String subMrchntNm) {
        this.subMrchntNm = subMrchntNm;
    }

    public String getSubMrchntAbbrNm() {
        return subMrchntAbbrNm;
    }

    public void setSubMrchntAbbrNm(String subMrchntAbbrNm) {
        this.subMrchntAbbrNm = subMrchntAbbrNm;
    }

    public String getSubMCC() {
        return subMCC;
    }

    public void setSubMCC(String subMCC) {
        this.subMCC = subMCC;
    }

    public String getSubMrchntIdTp() {
        return subMrchntIdTp;
    }

    public void setSubMrchntIdTp(String subMrchntIdTp) {
        this.subMrchntIdTp = subMrchntIdTp;
    }

    public String getSubMrchntIdNo() {
        return subMrchntIdNo;
    }

    public void setSubMrchntIdNo(String subMrchntIdNo) {
        this.subMrchntIdNo = subMrchntIdNo;
    }

    @Override
    public String toString() {
        return "SubMrchntInf{" +
                "subMrchntNo='" + subMrchntNo + '\'' +
                ", subMrchntWltId='" + subMrchntWltId + '\'' +
                ", subMrchntNm='" + subMrchntNm + '\'' +
                ", subMrchntAbbrNm='" + subMrchntAbbrNm + '\'' +
                ", subMCC='" + subMCC + '\'' +
                ", subMrchntIdTp='" + subMrchntIdTp + '\'' +
                ", subMrchntIdNo='" + subMrchntIdNo + '\'' +
                '}';
    }
}
