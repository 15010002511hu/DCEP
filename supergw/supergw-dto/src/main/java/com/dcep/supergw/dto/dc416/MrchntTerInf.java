package com.dcep.supergw.dto.dc416;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MrchntTerInf implements Serializable {

    @Length(min = 1,max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "TerNo")
    private String terNo;

    @Length(min = 1,max = 128)
    @JacksonXmlProperty(localName = "TerLctnInf")
    private String terLctnInf;

    @Length(min = 1,max = 40)
    @JacksonXmlProperty(localName = "PltfrmNm")
    private String pltfrmNm;

    @Length(min = 1,max = 128)
    @JacksonXmlProperty(localName = "MrchntBizAddr")
    private String mrchntBizAddr;

    @Length(min = 1,max = 149)
    @JacksonXmlProperty(localName = "TerDevcInf")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String terDevcInf;

    @Length(min = 1,max = 6)
    @JacksonXmlProperty(localName = "MrchntLctnCd")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mrchntLctnCd;
}
