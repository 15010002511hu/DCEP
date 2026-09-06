/*
 * pbcdci.cn Inc. Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc391;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 上链钱包信息
 * 
 * @author laimincai
 * @date 2023/12/29
 */
public class WltInf implements Serializable {

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


    public String getWltNm() {
        return wltNm;
    }

    public void setWltNm(String wltNm) {
        this.wltNm = wltNm;
    }

    @Override
    public String toString() {
        return "WltInf [wltId=" + wltId + ", wltNm=" + wltNm + "]";
    }
}
