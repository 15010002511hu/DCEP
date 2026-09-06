package com.dcep.supergw.dto.dc314;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : BizQryRef.java v 0.1 2021-04-14
 * @description :
 */
public class BizQryRef implements Serializable {
    /**
     * 原查询报文标识号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1,max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "QryRef")
    private String qryRef;

    /**
     * 原查询发起运营机构
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1,max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "QryNm")
    private String qryNm;

    /**
     * 查询处理状态
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1,max = 4)
    @Pattern(regexp = "PR00||PR01")
    @JacksonXmlProperty(localName = "QryRs")
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
        return "BizQryRef{" +
                "qryRef='" + qryRef + '\'' +
                ", qryNm='" + qryNm + '\'' +
                ", qryRs='" + qryRs + '\'' +
                '}';
    }
}
