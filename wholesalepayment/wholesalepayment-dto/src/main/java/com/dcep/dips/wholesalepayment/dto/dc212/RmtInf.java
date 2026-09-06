/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc212;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

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
    private static final long serialVersionUID = 7958024638349539188L;

    /*
     * Unstructured附言/Postscript/Value
     */
    @JacksonXmlProperty(localName = "Postscript")
    @Length(min = 1, max = 120)
    private String     postscript;

    /*
     * Unstructured备注/Remark/Value
     */
    @JacksonXmlProperty(localName = "Remark")
    @Length(min = 1, max = 512)
    private String            remark;
    
    /**
     * 居民类型
     */
    @JacksonXmlProperty(localName = "ResdtTp")
    @Pattern(regexp = "^[R][T][0-9]{2}||REST[0-9]{2}")
    private String            resdtTp;
    
    /**
     * 常驻国家/地区代码
     */
    @JacksonXmlProperty(localName = "ResdtCtryCd")
    @Length(min = 1, max = 3)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            resdtCtryCd;
    
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
            if(ustrd.contains("/Postscript/")){
                this.setPostscript(ustrd.substring("/Postscript/".length()));
            } else if(ustrd.contains("/Remark/")){
                this.setRemark(ustrd.substring("/Remark/".length()));
            } else if (ustrd.contains("/ResdtTp/")) {
                this.setResdtTp(ustrd.substring("/ResdtTp/".length()));
            } else if (ustrd.contains("/ResdtCtryCd/")) {
                this.setResdtCtryCd(ustrd.substring("/ResdtCtryCd/".length()));
            } else if (ustrd.contains("/RegrCtryCd/")) {
                this.setRegrCtryCd(ustrd.substring("/RegrCtryCd/".length()));
            } 
        }
    }


}
