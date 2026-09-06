package com.dcep.supergw.dto.dc305;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
@Setter
@Getter
@ToString
public class MrchntTerInf implements Serializable {
    /**
     * 受理终端地理位置
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "TerNo")
    private String terNo;
    /**
     * 受理终端地理位置
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 128)
    @JacksonXmlProperty(localName = "TerLctnInf")
    private String terLctnInf;
    /**
     * 网络交易平台名称
     */
    @Length(min = 1, max = 40)
    @JacksonXmlProperty(localName = "PltfrmNm")
    private String pltfrmNm;
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
