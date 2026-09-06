package com.dcep.supergw.dto.dc301;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : AcqAgtOmf.java v 0.1 2021-04-13
 * @description :
 */
public class AcqAgtInf implements Serializable {

    /**
     * 受理机构金融编码
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1,max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "AcqAgtInstnId")
    private String acqAgtInstnId;

    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1,max = 60)
    @JacksonXmlProperty(localName = "AcqAgtNm")
    private String acqAgtNm;

    public String getAcqAgtInstnId() {
        return acqAgtInstnId;
    }

    public void setAcqAgtInstnId(String acqAgtInstnId) {
        this.acqAgtInstnId = acqAgtInstnId;
    }

    public String getAcqAgtNm() {
        return acqAgtNm;
    }

    public void setAcqAgtNm(String acqAgtNm) {
        this.acqAgtNm = acqAgtNm;
    }

    @Override
    public String toString() {
        return "AcqAgtOmf{" +
                "acqAgtInstnId='" + acqAgtInstnId + '\'' +
                ", acqAgtNm='" + acqAgtNm + '\'' +
                '}';
    }
}
