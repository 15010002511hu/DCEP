package com.dcep.supergw.dto.dc301;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class DbtrInf {

    /**
     * 签约模式
     */
    @Pattern(regexp = "SPM00||SPM01")
    @NotBlank
    @JacksonXmlProperty(localName = "PtcMdTp")
    private String ptcMdTp;

    /**
     * 付款钱包信息
     */
    @Valid
    @JacksonXmlProperty(localName = "DbtrWltInf")
    private DbtrWltInf dbtrWltInf;

    /**
     * 付款企业信息
     */
    @Valid
    @JacksonXmlProperty(localName = "DbtrCoyInf")
    private DbtrCoyInf dbtrCoyInf;


    public String getPtcMdTp() {
        return ptcMdTp;
    }

    public void setPtcMdTp(String ptcMdTp) {
        this.ptcMdTp = ptcMdTp;
    }

    public DbtrWltInf getDbtrWltInf() {
        return dbtrWltInf;
    }

    public void setDbtrWltInf(DbtrWltInf dbtrWltInf) {
        this.dbtrWltInf = dbtrWltInf;
    }

    public DbtrCoyInf getDbtrCoyInf() {
        return dbtrCoyInf;
    }

    public void setDbtrCoyInf(DbtrCoyInf dbtrCoyInf) {
        this.dbtrCoyInf = dbtrCoyInf;
    }


    @Override
    public String toString() {
        return "DbtrInf{" +
            "ptcMdTp='" + ptcMdTp + '\'' +
            ", dbtrWltInf=" + dbtrWltInf +
            ", dbtrCoyInf=" + dbtrCoyInf +
            '}';
    }
}
