package com.dcep.supergw.dto.dc319;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : PmtChnlInf.java v 0.1 2023-01-04 Copyright 2023 PBCDCI ALL Rights
 * @description :
 */
@Getter
@Setter
@ToString
public class PmtChnlInf implements Serializable {
    private static final long serialVersionUID = -8204408686027548252L;

    @NotBlank
    @Pattern(regexp = "PCT02||PCT03||CT03||CT06")
    @JacksonXmlProperty(localName = "PmtChnlTp")
    String pmtChnlTp;

    @NotBlank
    @Length(min = 1, max = 20)
    @JacksonXmlProperty(localName = "PmtChnlNm")
    String pmtChnlNm;
}
