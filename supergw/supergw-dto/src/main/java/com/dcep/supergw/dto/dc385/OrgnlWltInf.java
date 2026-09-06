/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc385;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 原锁定钱包信息
 * 
 * @author laimincai
 * @date 2023/12/29
 */
public class OrgnlWltInf implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * 钱包ID
     */
    @JacksonXmlProperty(localName = "WltId")
    @NotBlank
    @Length(min = 1, max = 68)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String wltId;
    
    /**
     * 钱包等级
     */
    @JacksonXmlProperty(localName = "WltLvl")
    @NotBlank
    @Length(min = 4, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "WL01||WL02||WL03||WL04||WL05")
    private String wltLvl;

    /**
     * 钱包类型
     */
    @JacksonXmlProperty(localName = "WltTp")
    @NotBlank
    @Length(min = 4, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String wltTp;

    /**
     * 钱包名称
     */
    @JacksonXmlProperty(localName = "WltNm")
    @NotBlank
    @Length(min = 1, max = 60)
    private String wltNm;

    public String getWltId() {
        return wltId;
    }

    public void setWltId(String wltId) {
        this.wltId = wltId;
    }

    public String getWltTp() {
        return wltTp;
    }

    public void setWltTp(String wltTp) {
        this.wltTp = wltTp;
    }

    public String getWltLvl() {
        return wltLvl;
    }

    public void setWltLvl(String wltLvl) {
        this.wltLvl = wltLvl;
    }

    public String getWltNm() {
        return wltNm;
    }

    public void setWltNm(String wltNm) {
        this.wltNm = wltNm;
    }

    @Override
    public String toString() {
        return "OrgnlWltInf [wltId=" + wltId + ", wltTp=" + wltTp + ", wltLvl=" + wltLvl + ", wltNm=" + wltNm + "]";
    }
}
