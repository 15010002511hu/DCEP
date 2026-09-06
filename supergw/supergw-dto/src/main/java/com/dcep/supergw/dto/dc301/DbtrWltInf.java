package com.dcep.supergw.dto.dc301;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class DbtrWltInf {

    /**
     * 付钱钱包机构
     */
    @Length(min = 1,max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @NotBlank
    @JacksonXmlProperty(localName = "DbtrPtyId")
    private String dbtrPtyId;

    /**
     * 付款方名称
     */
    @Length(min = 1,max = 240)
    @JacksonXmlProperty(localName = "DbtrNm")
    private String dbtrNm;

    /**
     * 付钱方钱包id
     */
    @Length(min = 1,max = 68)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @NotBlank
    @JacksonXmlProperty(localName = "DbtrWltId")
    private String dbtrWltId;

    @Pattern(regexp = "WL01||WL02||WL03||WL04")
    @JacksonXmlProperty(localName = "DbtrWltLvl")
    private String dbtrWltLvl;

    @Pattern(regexp = "WT[0-9]{2}")
    @JacksonXmlProperty(localName = "DbtrWltTp")
    private String dbtrWltTp;

    @Length(min =1,max = 60)
    @JacksonXmlProperty(localName = "DbtrWltNm")
    private String dbtrWltNm;


    public String getDbtrPtyId() {
        return dbtrPtyId;
    }

    public void setDbtrPtyId(String dbtrPtyId) {
        this.dbtrPtyId = dbtrPtyId;
    }

    public String getDbtrNm() {
        return dbtrNm;
    }

    public void setDbtrNm(String dbtrNm) {
        this.dbtrNm = dbtrNm;
    }

    public String getDbtrWltId() {
        return dbtrWltId;
    }

    public void setDbtrWltId(String dbtrWltId) {
        this.dbtrWltId = dbtrWltId;
    }

    public String getDbtrWltLvl() {
        return dbtrWltLvl;
    }

    public void setDbtrWltLvl(String dbtrWltLvl) {
        this.dbtrWltLvl = dbtrWltLvl;
    }

    public String getDbtrWltTp() {
        return dbtrWltTp;
    }

    public void setDbtrWltTp(String dbtrWltTp) {
        this.dbtrWltTp = dbtrWltTp;
    }

    public String getDbtrWltNm() {
        return dbtrWltNm;
    }

    public void setDbtrWltNm(String dbtrWltNm) {
        this.dbtrWltNm = dbtrWltNm;
    }

    @Override
    public String toString() {
        return "DbtrWltInf{" +
            "dbtrPtyId='" + dbtrPtyId + '\'' +
            ", dbtrNm='" + dbtrNm + '\'' +
            ", dbtrWltId='" + dbtrWltId + '\'' +
            ", dbtrWltLvl='" + dbtrWltLvl + '\'' +
            ", dbtrWltTp='" + dbtrWltTp + '\'' +
            ", dbtrWltNm='" + dbtrWltNm + '\'' +
            '}';
    }
}
