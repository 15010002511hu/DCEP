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
public class PmtChnlInf implements Serializable {
    private static final long serialVersionUID = 8154360686870582185L;
    /**
     * 交易类型
     */
    @JacksonXmlProperty(localName = "PmtChnlTp")
    @NotBlank
    @Pattern(regexp = "PCT[0-9]{2}||CT[0-9]{2}")
    private String pmtChnlTp;

    /**
     * 交易类型
     */
    @JacksonXmlProperty(localName = "PmtChnlNm")
    @NotBlank
    @Length(min = 1, max = 20)
    private String pmtChnlNm;
}
