package com.dcep.supergw.dto.dc312;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class RdrctInf {
    /**
     * 跳转地址
     */
    @Length(min = 1,max = 1024)
    @NotBlank
    @JacksonXmlProperty(localName = "RdrctUrl")
    String rdrctUrl;
}
