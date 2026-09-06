/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc262;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author chenxf
 * @version $Id: RspsnInf.java
 */
@JacksonXmlRootElement(localName = "RspsnInf")
@Getter
@Setter
@ToString
public class RspsnInf implements Serializable {
    /**  */
    private static final long serialVersionUID = -1953855599262307705L;

    /**
     * 业务回执状态
     */
    @JacksonXmlProperty(localName = "RspsnSts")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 4, max = 4)
    @Pattern(regexp = "PR01||PR02")
    private String            rspsnSts;

    /**
     * 业务拒绝码
     */
    @JacksonXmlProperty(localName = "RjctCd")
    @Pattern(regexp = "^[R][0-9]{3}$")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            rjctCd;

    /**
     * 业务拒绝信息 允许中文
     */
    @JacksonXmlProperty(localName = "RjctInf")
    @Length(min = 1, max = 105)
    private String            rjctInf;

}
