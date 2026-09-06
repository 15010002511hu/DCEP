/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc201;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author liuqi
 * @version $Id: SchmeNm.java, v 0.1 2019年8月23日 下午7:54:40 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "SchmeNm")
@Getter
@Setter
@ToString
public class SchmeNm implements Serializable {
    /**  */
    private static final long serialVersionUID = 2107395519742006794L;
    /*
     * 付款人钱包等级/收款人钱包等级
     */
    @JacksonXmlProperty(localName = "Prtry")
    @NotBlank
    @Pattern(regexp = "WL01||WL02||WL03||WL04||WL05")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            prtry;

}
