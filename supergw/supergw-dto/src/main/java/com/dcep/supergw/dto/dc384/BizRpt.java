/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc384;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 应答的原业务信息
 * 
 * @author laimincai
 * @date 2023/12/29
 */
public class BizRpt implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /*
     * 业务回执状态 不能为空，码值长度4
     */
    @JacksonXmlProperty(localName = "RspsnSts")
    @NotBlank
    @Pattern(regexp = "PR00||PR01")
    @Length(min = 4, max = 4)
    private String rspsnSts;

    /*
     * 业务拒绝码 标签可以不存在，存在必须有值
     */
    @JacksonXmlProperty(localName = "RjctCd")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 4, max = 4)
    @Pattern(regexp = "R[0-9]{3}")
    private String rjctCd;

    /*
     * 业务拒绝信息
     */
    @JacksonXmlProperty(localName = "RjctInf")
    @Length(min = 1, max = 105)
    private String rjctInf;

    public String getRspsnSts() {
        return rspsnSts;
    }

    public void setRspsnSts(String rspsnSts) {
        this.rspsnSts = rspsnSts;
    }

    public String getRjctCd() {
        return rjctCd;
    }

    public void setRjctCd(String rjctCd) {
        this.rjctCd = rjctCd;
    }

    public String getRjctInf() {
        return rjctInf;
    }

    public void setRjctInf(String rjctInf) {
        this.rjctInf = rjctInf;
    }

    @Override
    public String toString() {
        return "BizRpt [rspsnSts=" + rspsnSts + ", rjctCd=" + rjctCd + ", rjctInf=" + rjctInf + "]";
    }
}
