package com.dcep.supergw.dto.dc303;

import com.dcep.common.validator.Priority;
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
public class MrchntTerInf implements Serializable {
    /**
     * 受理终端地理位置
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 128)
    @JacksonXmlProperty(localName = "TerLctnInf")
    private String terLctnInf;

    /**
     * 商户经营地址
     */
    @Length(min = 1, max = 128)
    @JacksonXmlProperty(localName = "MrchntBizAddr")
    private String mrchntBizAddr;

    @Length(min = 1, max = 6)
    @JacksonXmlProperty(localName = "MrchntLctnCd")
    @Pattern(regexp = "\\d{6}")
    private String mrchntLctnCd;

    /**
     * 受理终端设备信息
     */
    @Length(min = 1, max = 149)
    @JacksonXmlProperty(localName = "TerDevcInf")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String terDevcInf;

}
