package com.dcep.supergw.dto.dc325;

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
public class PtcInf implements Serializable {
    private static final long serialVersionUID = 5811802366496486784L;
    /**
     * 签约协议号
     */
    @JacksonXmlProperty(localName = "PtcId")
    @Length(min = 1, max = 34)
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String ptcId;
    /**
     * 商户证件类型
     */
    @JacksonXmlProperty(localName = "MrchntIdTp")
    @Pattern(regexp = "IT[0-9]{2}")
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mrchntIdTp;
    /**
     * 商户证件编码
     */
    @JacksonXmlProperty(localName = "MrchntIdNo")
    @Length(min = 1, max = 32)
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mrchntIdNo;
    /**
     * 场景ID
     */
    @JacksonXmlProperty(localName = "SceneId")
    @Length(min = 1, max = 10)
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String sceneId;
}
