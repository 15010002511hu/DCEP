package com.dcep.supergw.dto.dc073;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ReqInf implements Serializable {

    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SvcInstnId")
    String svcInstnId;

    @NotBlank
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "MsgSndCd")
    String msgSndCd;

    @NotBlank
    @Length(min = 1, max = 6)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "MsgVerifCd")
    String msgVerifCd;

}
