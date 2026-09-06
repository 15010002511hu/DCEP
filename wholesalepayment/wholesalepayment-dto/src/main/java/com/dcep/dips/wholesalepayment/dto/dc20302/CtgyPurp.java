/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc20302;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
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
@AllArgsConstructor
@NoArgsConstructor
public class CtgyPurp implements Serializable{/**  */
    private static final long serialVersionUID = 8981627351690651802L;
    /**
     * 业务类型编码
     */
    @JacksonXmlProperty(localName = "Prtry")
    @NotBlank
    @Length(min = 1, max = 4)
    private String                  prtry;
}
