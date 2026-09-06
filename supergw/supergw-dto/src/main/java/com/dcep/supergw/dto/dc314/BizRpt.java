package com.dcep.supergw.dto.dc314;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : BizRpt.java v 0.1 2021-04-14
 * @description :
 */
public class BizRpt implements Serializable {
    /**
     * 原业务状态
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 4, max = 4)
    @Pattern(regexp = "PR[0-9]{2}")
    @JacksonXmlProperty(localName = "TrnRs")
    private String trnRs;


    @Valid
    @JacksonXmlProperty(localName = "Rsn")
    private Rsn rsn;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "OrgnlTxInf")
    private OrgnlTxInf orgnlTxInf;

    @Valid
    @JacksonXmlProperty(localName = "PrmtInf")
    private PrmtInf prmtInf;


    public String getTrnRs() {
        return trnRs;
    }

    public void setTrnRs(String trnRs) {
        this.trnRs = trnRs;
    }

    public Rsn getRsn() {
        return rsn;
    }

    public void setRsn(Rsn rsn) {
        this.rsn = rsn;
    }

    public OrgnlTxInf getOrgnlTxInf() {
        return orgnlTxInf;
    }

    public void setOrgnlTxInf(OrgnlTxInf orgnlTxInf) {
        this.orgnlTxInf = orgnlTxInf;
    }

    public PrmtInf getPrmtInf() {
        return prmtInf;
    }

    public void setPrmtInf(PrmtInf prmtInf) {
        this.prmtInf = prmtInf;
    }

    @Override
    public String toString() {
        return "BizRpt{" +
                "trnRs='" + trnRs + '\'' +
                ", rsn=" + rsn +
                ", orgnlTxInf=" + orgnlTxInf +
                '}';
    }
}
