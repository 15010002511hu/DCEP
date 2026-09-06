/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc202;

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
 * 
 * @author liuqi
 * @version $Id: RmtInf.java, v 0.1 2019年8月23日 下午8:14:08 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "RmtInf")
@Setter
@Getter
@ToString
public class RmtInf implements Serializable {
  
     /**  */
    private static final long serialVersionUID = 5047868775165449742L;
    /** 收款机构流水号/CdtrBankId/Value 
     */
    @JacksonXmlProperty(localName = "CdtrBankId")
    @Length(max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String     cdtrBankId;

    /** 收款人名称/CdtrNm/Value
     */
    @JacksonXmlProperty(localName = "CdtrNm")
    @Length(min = 0, max = 60)
    private String     cdtrNm;

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
    public RmtInf(List<String> ustrds){
        for(String ustrd: ustrds){
            if(ustrd.contains("/CdtrBankId/")){
                this.setCdtrBankId(ustrd.substring("/CdtrBankId/".length()));
            }else if(ustrd.contains("/CdtrNm/")){
                this.setCdtrNm(ustrd.substring("/CdtrNm/".length()));
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
