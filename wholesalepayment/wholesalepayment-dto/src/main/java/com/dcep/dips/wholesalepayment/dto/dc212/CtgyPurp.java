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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author liuqi
 * @version $Id: CtgyPurp.java, v 0.1 2019年8月23日 下午7:37:30 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "CtgyPurp")
@Getter
@Setter
@ToString
public class CtgyPurp implements Serializable{/**  */
    private static final long serialVersionUID = 8981627351690651802L;

    /**
     * 原业务类型编码
     */
    @JacksonXmlProperty(localName = "Prtry")
    @NotBlank
    @Length(min = 3, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  prtry;
}
