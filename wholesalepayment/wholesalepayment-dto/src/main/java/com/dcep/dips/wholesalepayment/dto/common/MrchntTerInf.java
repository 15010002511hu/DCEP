package com.dcep.dips.wholesalepayment.dto.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 商户受理终端信息
 *
 * @author caoxiaogai
 *
 */
@JacksonXmlRootElement(localName = "MrchntTerInf")
@Setter
@Getter
@ToString
public class MrchntTerInf implements Serializable {

    private static final long serialVersionUID = -2915320423401117121L;

    /**
     * 受理终端编号
     */
    @JacksonXmlProperty(localName = "TerNo")
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            terNo;

    /**
     * 受理终端地址位置
     */
    @JacksonXmlProperty(localName = "TerLctnInf")
    @Length(min = 1, max = 128)
    private String            terLctnInf;

    /**
     * 网络交易平台名称(商户属性为MP02或MP03时必填)
     */
    @JacksonXmlProperty(localName = "PltfrmNm")
    @Length(min = 1, max = 40)
    private String            pltfrmNm;

    /**
     * 商户经营地址(商户属性为MP01或MP03时必填)
     */
    @JacksonXmlProperty(localName = "MrchntBizAddr")
    @Length(min = 1, max = 128)
    private String            mrchntBizAddr;

    /**
     * 受理终端编号
     */
    @JacksonXmlProperty(localName = "TerDevcInf")
    @Length(min = 1, max = 149)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            terDevcInf;

    /**
     * 商户所在地代码
     */
    @JacksonXmlProperty(localName = "MrchntLctnCd")
    @Length(min = 1, max = 6)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            mrchntLctnCd;

}
