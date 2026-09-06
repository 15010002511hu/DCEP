package com.dcep.supergw.dto.dc038;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CtrctCallParam implements Serializable {
    @JacksonXmlProperty(localName = "CallPth")
    @NotBlank
    @Length(min = 1, max = 128)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String callPth;

    @JacksonXmlProperty(localName = "CallParam")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String callParam;
}
