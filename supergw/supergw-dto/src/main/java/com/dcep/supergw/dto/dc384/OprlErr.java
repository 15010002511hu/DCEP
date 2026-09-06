/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc384;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 应答拒绝信息
 * 
 * @author laimincai
 * @date 2023/12/29
 */
public class OprlErr implements Serializable {

    private static final long serialVersionUID = 1L;

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
        return "OprlErr [rjctCd=" + rjctCd + ", rjctInf=" + rjctInf + "]";
    }

}
