package com.dcep.supergw.dto.dc314;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : Rsn.java v 0.1 2021-04-14
 * @description :
 */
public class Rsn implements Serializable {

    /**
     * 业务拒绝码
     */
    @Length(min = 4, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "R[0-9]{3}")
    @JacksonXmlProperty(localName = "RjctCd")
    private String rjctCd;

    /**
     * 业务信息
     */
    @Length(min = 1, max = 105)
    @JacksonXmlProperty(localName = "RjctInf")
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
        return "Rsn{" +
                "rjctCd='" + rjctCd + '\'' +
                ", rjctInf='" + rjctInf + '\'' +
                '}';
    }
}
