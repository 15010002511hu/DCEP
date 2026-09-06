/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc384;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 原业务信息
 * 
 * @author laimincai
 * @date 2023/12/29
 */
public class BizQryRef implements Serializable {

    private static final long serialVersionUID = 1L;

    @JacksonXmlProperty(localName = "QryRef")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String qryRef;
    
    @JacksonXmlProperty(localName = "QryNm")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String qryNm;
    
    @JacksonXmlProperty(localName = "QryRs")
    @NotBlank
    @Pattern(regexp = "PR00||PR01")
    private String qryRs;

    public String getQryRef() {
        return qryRef;
    }

    public void setQryRef(String qryRef) {
        this.qryRef = qryRef;
    }

    public String getQryNm() {
        return qryNm;
    }

    public void setQryNm(String qryNm) {
        this.qryNm = qryNm;
    }

    public String getQryRs() {
        return qryRs;
    }

    public void setQryRs(String qryRs) {
        this.qryRs = qryRs;
    }

    @Override
    public String toString() {
        return "BizQryRef [qryRef=" + qryRef + ", qryNm=" + qryNm + ", qryRs=" + qryRs + "]";
    }
}
