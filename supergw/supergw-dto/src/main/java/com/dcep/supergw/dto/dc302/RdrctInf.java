package com.dcep.supergw.dto.dc302;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class RdrctInf implements Serializable {

    /**
     * 跳转地址
     */
    @Length(min = 1,max = 1024)
    @NotBlank
    @JacksonXmlProperty(localName = "RdrctUrl")
    String rdrctUrl;

}
