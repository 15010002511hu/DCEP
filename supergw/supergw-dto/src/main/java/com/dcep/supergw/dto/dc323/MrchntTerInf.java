package com.dcep.supergw.dto.dc323;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class MrchntTerInf implements Serializable {
    private static final long serialVersionUID = -6692122255569370891L;
    /**
     * 受理终端编号
     */
    @JacksonXmlProperty(localName = "TerNo")
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String terNo;
    /**
     * 受理终端地理位置
     */
    @JacksonXmlProperty(localName = "TerLctnInf")
    @Length(min = 1, max = 128)
    private String terLctnInf;
    /**
     * 网络交易平台名称
     */
    @JacksonXmlProperty(localName = "PltfrmNm")
    @Length(min = 1, max = 40)
    private String PltfrmNm;
    /**
     * 商户经营地址
     */
    @JacksonXmlProperty(localName = "MrchntBizAddr")
    @Length(min = 1, max = 128)
    private String mrchntBizAddr;
    /**
     * 受理终端设备信息
     */
    @JacksonXmlProperty(localName = "TerDevcInf")
    @Length(min = 1, max = 149)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String terDevcInf;
}
