package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * 组件ReportOrError
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:28:15
 */
@Data
public class RptOrErr implements Serializable {
    /**
     * 组件--BusinessReport
     */
    @JacksonXmlProperty(
            localName = "BizRpt"
    )
    @Valid
    private BizRpt bizRpt;

    /**
     * 组件--OperationalError
     */
    @JacksonXmlProperty(
            localName = "OprlErr"
    )
    @Valid
    private OprlErr oprlErr;
}
