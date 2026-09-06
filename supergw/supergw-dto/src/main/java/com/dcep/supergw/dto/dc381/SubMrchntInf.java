/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc381;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 二级商户信息
 * 
 * @author laimincai
 * @date 2023/12/29
 */
public class SubMrchntInf implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 二级商户编码
     */
    @JacksonXmlProperty(localName = "SubMrchntNo")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            subMrchntNo;

    /**
     * 二级商户钱包ID
     */
    @JacksonXmlProperty(localName = "SubMrchntWltId")
    @Length(min = 1, max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            subMrchntWltId;

    /**
     * 二级商户名称
     */
    @JacksonXmlProperty(localName = "SubMrchntNm")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 60)
    private String subMrchntNm;

    /**
     * 二级商户简称
     */
    @JacksonXmlProperty(localName = "SubMrchntAbbrNm")
    @Length(min = 1, max = 30)
    private String subMrchntAbbrNm;

    /**
     * 二级商户类别代码
     */
    @JacksonXmlProperty(localName = "SubMCC")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String subMCC;

    /**
     * 二级商户证件类型
     */
    @JacksonXmlProperty(localName = "SubMrchntIdTp")
    @Pattern(regexp = "^[I][T][0-9]{2}")
    private String subMrchntIdTp;

    /**
     * 二级商户证件编码
     */
    @JacksonXmlProperty(localName = "SubMrchntIdNo")
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
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
        return "SubMrchntInf [subMrchntNo=" + subMrchntNo + ", subMrchntWltId=" + subMrchntWltId + ", subMrchntNm="
                + subMrchntNm + ", subMrchntAbbrNm=" + subMrchntAbbrNm + ", subMCC=" + subMCC + ", subMrchntIdTp="
                + subMrchntIdTp + ", subMrchntIdNo=" + subMrchntIdNo + "]";
    }  
}
