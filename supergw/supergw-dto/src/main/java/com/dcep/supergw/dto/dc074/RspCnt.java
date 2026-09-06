package com.dcep.supergw.dto.dc074;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RspCnt implements Serializable {

    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min =1,max = 64)
    @NotBlank(groups = Priority.Highest.class)
    @JacksonXmlProperty(localName = "CtrctId")
    private String ctrctId;

    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "SCOCT[0-9]{2}")
    @JacksonXmlProperty(localName = "CmdTp")
    private String cmdTp;

    @NotBlank
    @Length(min =1,max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "CtrctSgntrRcrdId")
    String ctrctSgntrRcrdId;

    @Valid
    @JacksonXmlProperty(localName = "CtrctRspnInf")
    CtrctRspnInf ctrctRspnInf;


}
