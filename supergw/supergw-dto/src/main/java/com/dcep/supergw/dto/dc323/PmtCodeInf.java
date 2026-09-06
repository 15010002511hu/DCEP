package com.dcep.supergw.dto.dc323;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class PmtCodeInf implements Serializable {
    private static final long serialVersionUID = 822274324890737485L;
    /**
     * 订单号
     */
    @JacksonXmlProperty(localName = "PmtCode")
    @Length(min = 1, max = 34)
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String pmtCode;
}
