/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc961;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class CertInf implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /*
     * 数字信封加密算法类型
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "SM2withSM3")
    @JacksonXmlProperty(localName = "NcrptnAlgoTp")
    private String ncrptnAlgoTp;
    
    /*
     * 机构分散因子
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 2048)
    @JacksonXmlProperty(localName = "NcrptnCert")
    private String ncrptnCert;

    public String getNcrptnAlgoTp() {
        return ncrptnAlgoTp;
    }

    public void setNcrptnAlgoTp(String ncrptnAlgoTp) {
        this.ncrptnAlgoTp = ncrptnAlgoTp;
    }

    public String getNcrptnCert() {
        return ncrptnCert;
    }

    public void setNcrptnCert(String ncrptnCert) {
        this.ncrptnCert = ncrptnCert;
    }

    @Override
    public String toString() {
        return "CertInf [ncrptnAlgoTp=" + ncrptnAlgoTp + ", ncrptnCert=" + ncrptnCert + "]";
    }
}
