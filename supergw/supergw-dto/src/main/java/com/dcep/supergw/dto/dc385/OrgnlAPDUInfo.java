/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc385;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 *  APDU信息
 * 
 * @author laimincai
 * @date 2023/12/29
 */
public class OrgnlAPDUInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * APDU响应类型
     */
    @JacksonXmlProperty(localName = "APDURespType")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 4)
    private String            apduRespType;

    /**
     * APDU响应类型
     */
    @JacksonXmlProperty(localName = "APDURespData")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 1024)
    private String            apduRespData;

    public String getApduRespType() {
        return apduRespType;
    }

    public void setApduRespType(String apduRespType) {
        this.apduRespType = apduRespType;
    }

    public String getApduRespData() {
        return apduRespData;
    }

    public void setApduRespData(String apduRespData) {
        this.apduRespData = apduRespData;
    }

    @Override
    public String toString() {
        return "OrgnlAPDUInfo [apduRespType=" + apduRespType + ", apduRespData=" + apduRespData + "]";
    }
}
