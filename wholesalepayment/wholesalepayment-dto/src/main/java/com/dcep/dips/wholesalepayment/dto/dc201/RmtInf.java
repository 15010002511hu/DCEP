/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc201;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 附言对象
 * @author liuqi
 * @version $Id: RmtInf.java, v 0.1 2019年8月23日 下午8:14:08 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "RmtInf")
@Setter
@Getter
@ToString
public class RmtInf implements Serializable {
    /**  */
    private static final long serialVersionUID = 7958024638349539188L;
    /*
     * Unstructured附言/Postscript/Value
     */
    @JacksonXmlProperty(localName = "Postscript")
    @Length(min = 1, max = 300)
    private String     postscript;

    /*
     * Unstructured备注/Remark/Value
     */
    @JacksonXmlProperty(localName = "Remark")
    @Length(min = 1, max = 120)
    private String            remark;

    /*
     * Unstructured备注/MaskedTel/Value 掩码手机号
     */
    @JacksonXmlProperty(localName = "MaskedTel")
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            maskedTel;

    /*
     * Unstructured备注/MaskedEmail/Value 掩码邮箱
     */
    @JacksonXmlProperty(localName = "MaskedEmail")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            maskedEmail;
    
    /*
     * Unstructured备注/ScanId/Value 扫码转钱标识
     */
    @JacksonXmlProperty(localName = "ScanId")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            scanId;
    
    /*
     * Unstructured/RedPktId/Value 红包ID
     */
    @JacksonXmlProperty(localName = "RedPktId")
    @Length(min = 1, max = 36)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            redPktId;
    
    /**
     * 订单号
     */
    @JacksonXmlProperty(localName = "OrdrNo")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String ordrNo;
    
    /**
     * 转钱流水号
     */
    @JacksonXmlProperty(localName = "TrfSrlNo")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String trfSrlNo;

    /**
     * 居民类型
     */
    @JacksonXmlProperty(localName = "ResdtTp")
    @Pattern(regexp = "^[R][T][0-9]{2}||REST[0-9]{2}")
    private String resdtTp;

    /**
     * 常驻国家/地区代码
     */
    @JacksonXmlProperty(localName = "ResdtCtryCd")
    @Length(min = 1, max = 3)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String resdtCtryCd;

    /**
     * 钱包注册手机号所在国家/地区代码
     */
    @JacksonXmlProperty(localName = "RegrCtryCd")
    @Length(min = 1, max = 3)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            regrCtryCd;

    public RmtInf(){

    }

    /**
     * 通过List构造RmtInf对象，不含标签
     * @param ustrds
     */
    public RmtInf(List<String> ustrds) {
        for (String ustrd : ustrds) {
            if (ustrd.contains("/Postscript/")) {
                this.setPostscript(ustrd.substring("/Postscript/".length()));
            } else if (ustrd.contains("/Remark/")) {
                this.setRemark(ustrd.substring("/Remark/".length()));
            } else if (ustrd.contains("/MaskedTel/")) {
                this.setMaskedTel(ustrd.substring("/MaskedTel/".length()));
            } else if (ustrd.contains("/MaskedEmail/")) {
                this.setMaskedEmail(ustrd.substring("/MaskedEmail/".length()));
            } else if (ustrd.contains("/ScanId/")) {
                this.setScanId(ustrd.substring("/ScanId/".length()));
            } else if (ustrd.contains("/RedPktId/")) {
                this.setRedPktId(ustrd.substring("/RedPktId/".length()));
            } else if (ustrd.contains("/OrdrNo/")) {
                this.setOrdrNo(ustrd.substring("/OrdrNo/".length()));
            } else if (ustrd.contains("/TrfSrlNo/")) {
                this.setTrfSrlNo(ustrd.substring("/TrfSrlNo/".length()));
            }else if (ustrd.contains("/ResdtTp/")) {
                this.setResdtTp(ustrd.substring("/ResdtTp/".length()));
            }else if (ustrd.contains("/ResdtCtryCd/")) {
                this.setResdtCtryCd(ustrd.substring("/ResdtCtryCd/".length()));
            }else if (ustrd.contains("/RegrCtryCd/")) {
                this.setRegrCtryCd(ustrd.substring("/RegrCtryCd/".length()));
            }
        }
    }

}
