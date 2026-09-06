package com.dcep.supergw.dto.dc305;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.validation.constraints.NotBlank;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : ExInf.java v 0.1 2022-10-18
 * Copyright 2022 PBCDCI ALL Rights
 * @description :
 */
@ToString
public class ExInf {

    @JacksonXmlProperty(localName = "APDURespType")
    @Length(min = 1,max = 4)
    @NotBlank
    private String apduRespType;

    @JacksonXmlProperty(localName = "APDURespData")
    @Length(min = 1,max = 1024)
    @NotBlank
    private String apduRespData;

    @JsonIgnore
    public String getApduRespType() {
        return apduRespType;
    }

    public void setApduRespType(String apduRespType) {
        this.apduRespType = apduRespType;
    }

    @JsonIgnore
    public String getApduRespData() {
        return apduRespData;
    }

    public void setApduRespData(String apduRespData) {
        this.apduRespData = apduRespData;
    }
}
