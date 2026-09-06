package com.dcep.supergw.dto.dc071;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ReqInf implements Serializable {

    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "OprInstnId")
    String oprInstnId;

    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SvcInstnId")
    String svcInstnId;

    @NotBlank
    @Pattern(regexp = "CIT01||CIT02")
    @JacksonXmlProperty(localName = "CustIdType")
    String custIdType;

    @NotBlank
    @Length(min = 1, max = 68)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "CustId")
    String custId;

    @NotBlank
    @Pattern(regexp = "SCOCT[0-9]{2}")
    @JacksonXmlProperty(localName = "CmdTp")
    String cmdTp;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "CmdCnt")
    CmdCnt cmdCnt;
}
