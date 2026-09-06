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

public class IndstryInf implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /*
     * 行业标识 
     * 01：公共交通领域
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "01")
    @JacksonXmlProperty(localName = "IndstryId")
    private String indstryId;
    
    /*
     * 行业标识扩展字段
     */
    @Length(min = 1, max = 8)
    @JacksonXmlProperty(localName = "IndstryXtndFld")
    private String indstryXtndFld;
    
    /*
     * 机构分散因子
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 32)
    @JacksonXmlProperty(localName = "InstnFctrId")
    private String instnFctrId;

    public String getIndstryId() {
        return indstryId;
    }

    public void setIndstryId(String indstryId) {
        this.indstryId = indstryId;
    }

    public String getIndstryXtndFld() {
        return indstryXtndFld;
    }

    public void setIndstryXtndFld(String indstryXtndFld) {
        this.indstryXtndFld = indstryXtndFld;
    }

    public String getInstnFctrId() {
        return instnFctrId;
    }

    public void setInstnFctrId(String instnFctrId) {
        this.instnFctrId = instnFctrId;
    }

    @Override
    public String toString() {
        return "IndstryInf [indstryId=" + indstryId + ", indstryXtndFld=" + indstryXtndFld + ", instnFctrId="
                + instnFctrId + "]";
    }
}
