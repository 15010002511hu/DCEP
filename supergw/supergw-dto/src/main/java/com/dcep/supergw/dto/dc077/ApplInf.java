package com.dcep.supergw.dto.dc077;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ApplInf implements Serializable {
    @NotBlank
    @Length(min = 1,max = 15)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "MT")
    String mT;

    @NotBlank
    @Length(min = 1,max = 126)
    @JacksonXmlProperty(localName = "MsgDesc")
    String msgDesc;
}
