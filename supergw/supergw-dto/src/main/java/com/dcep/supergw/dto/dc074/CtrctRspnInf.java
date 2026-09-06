package com.dcep.supergw.dto.dc074;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CtrctRspnInf implements Serializable {

    @NotBlank
    @Length(min = 1,max = 8)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "CtrctRplyVal")
    String ctrctRplyVal;

    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "CtrctRplyInf")
    String ctrctRplyInf;
}
