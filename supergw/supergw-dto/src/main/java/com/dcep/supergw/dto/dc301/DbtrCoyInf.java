package com.dcep.supergw.dto.dc301;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class DbtrCoyInf {

    /**
     * 付款方钱包所属运营机构
     */
    @Length(min = 1,max = 14)
    @NotBlank
    @JacksonXmlProperty(localName = "DbtrPtyId")
    private String dbtrPtyId;


    /**
     * 付款方企业证件类型
     */
    @Pattern(regexp = "IT[0-9]{2}")
    @NotBlank
    @JacksonXmlProperty(localName = "DbtrCoyIdTp")
    private String dbtrCoyIdTp;

    /**
     * 付款方企业证件编码
     */
    @Length(min =1,max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @NotBlank
    @JacksonXmlProperty(localName = "DbtrCoyIdNo")
    private String dbtrCoyIdNo;

    /**
     * 付款方企业名称
     */
    @Length(min =1,max = 60)
    @NotBlank
    @JacksonXmlProperty(localName = "DbtrCoyNm")
    private String dbtrCoyNm;

    /**
     * 付款方企业简称
     */
    @Length(min =1,max = 30)
    @JacksonXmlProperty(localName = "DbtrCoyAbbrNm")
    private String dbtrCoyAbbrNm;

    public String getDbtrCoyIdTp() {
        return dbtrCoyIdTp;
    }

    public void setDbtrCoyIdTp(String dbtrCoyIdTp) {
        this.dbtrCoyIdTp = dbtrCoyIdTp;
    }

    public String getDbtrCoyIdNo() {
        return dbtrCoyIdNo;
    }

    public void setDbtrCoyIdNo(String dbtrCoyIdNo) {
        this.dbtrCoyIdNo = dbtrCoyIdNo;
    }

    public String getDbtrCoyNm() {
        return dbtrCoyNm;
    }

    public void setDbtrCoyNm(String dbtrCoyNm) {
        this.dbtrCoyNm = dbtrCoyNm;
    }

    public String getDbtrCoyAbbrNm() {
        return dbtrCoyAbbrNm;
    }

    public void setDbtrCoyAbbrNm(String dbtrCoyAbbrNm) {
        this.dbtrCoyAbbrNm = dbtrCoyAbbrNm;
    }

    public String getDbtrPtyId() {
        return dbtrPtyId;
    }

    public void setDbtrPtyId(String dbtrPtyId) {
        this.dbtrPtyId = dbtrPtyId;
    }

    @Override
    public String toString() {
        return "DbtrCoyInf{" +
            "dbtrCoyIdTp='" + dbtrCoyIdTp + '\'' +
            "dbtrPtyId='" + dbtrPtyId + '\'' +
            ", dbtrCoyIdTp='" + dbtrCoyIdTp + '\'' +
            ", dbtrCoyIdNo='" + dbtrCoyIdNo + '\'' +
            ", dbtrCoyNm='" + dbtrCoyNm + '\'' +
            ", dbtrCoyAbbrNm='" + dbtrCoyAbbrNm + '\'' +
            '}';
    }
}

