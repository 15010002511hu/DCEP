package com.dcep.supergw.dto.dc069;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ArgmtTxt implements Serializable {

    @NotBlank
    @Length(min = 1, max = 256)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "URL")
    String uRL;

    @NotBlank
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "Hash")
    String hash;

    @Length(min = 1, max = 512)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "EncTrsKey")
    String encTrsKey;


}
