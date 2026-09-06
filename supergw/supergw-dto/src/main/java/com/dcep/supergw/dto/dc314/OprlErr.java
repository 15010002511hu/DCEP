package com.dcep.supergw.dto.dc314;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : OprlErr.java v 0.1 2021-04-14
 * @description : 应答拒绝信息
 */
public class OprlErr implements Serializable {

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "Err")
    private Err err;

    /**
     *业务拒绝信息
     */
    @Length(min = 1, max = 105)
    @JacksonXmlProperty(localName = "RjctInf")
    private String rjctInf;

    public Err getErr() {
        return err;
    }

    public void setErr(Err err) {
        this.err = err;
    }

    public String getRjctInf() {
        return rjctInf;
    }

    public void setRjctInf(String rjctInf) {
        this.rjctInf = rjctInf;
    }

    @Override
    public String toString() {
        return "OprlErr{" +
                "err=" + err +
                ", rjctInf='" + rjctInf + '\'' +
                '}';
    }

    public class Err implements Serializable {

        /**
         * 业务拒绝信息
         */
        @Length(min = 1, max = 4)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @Pattern(regexp = "R[0-9]{3}")
        @JacksonXmlProperty(localName = "RjctCd")
        private String rjctCd;

        public String getRjctCd() {
            return rjctCd;
        }

        public void setRjctCd(String rjctCd) {
            this.rjctCd = rjctCd;
        }

        @Override
        public String toString() {
            return "Err{" +
                    "rjctCd='" + rjctCd + '\'' +
                    '}';
        }
    }


}
