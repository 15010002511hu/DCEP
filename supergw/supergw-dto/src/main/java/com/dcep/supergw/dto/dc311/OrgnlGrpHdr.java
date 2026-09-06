package com.dcep.supergw.dto.dc311;

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
public class OrgnlGrpHdr implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5271700864501615066L;
    /**
     * 原报文标识号
     */
    @JacksonXmlProperty(localName = "OrgnlMsgId")
    @NotBlank
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String orgnlMsgId;
    /**
     * 原发起机构
     */
    @JacksonXmlProperty(localName = "OrgnlInstgPty")
    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String orgnlInstgPty;

    /**
     * 原报文编号
     */
    @JacksonXmlProperty(localName = "OrgnlMT")
    @NotBlank
    @Length(min = 1, max = 15)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String orgnlMT;

    /**
     * 原商户号
     */
    @JacksonXmlProperty(localName = "OrgnlMrchntNo")
    @NotBlank
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String orgnlMrchntNo;

    /**
     * 原商户订单号
     */
    @JacksonXmlProperty(localName = "OrgnlOutOrdrNo")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String orgnlOutOrdrNo;
    /**
     * 原订单号
     */
    @JacksonXmlProperty(localName = "OrgnlOrdrNo")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String orgnlOrdrNo;

    /**
     * 收/付标识
     */
    @JacksonXmlProperty(localName = "RPFlg")
    @Pattern(regexp = "RPF01||RPF02||RP01||RP02")
    private String rPFlg;
}
