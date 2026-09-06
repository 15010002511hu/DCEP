/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc203;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author liuqi
 * @version $Id: FinInstnId.java, v 0.1 2019年8月23日 下午7:43:15 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "FinInstnId")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FinInstnId implements Serializable {
    /**  */
    private static final long serialVersionUID = -3727505539513019496L;
    /**
     * FinancialInstitutionIdentification
     */
    @JacksonXmlProperty(localName = "ClrSysMmbId")
    @Valid
    @NotNull
    private ClrSysMmbId       clrSysMmbId;
    
    /**
     * LEI编码
     */
    @JacksonXmlProperty(localName = "LEI")
    @NotBlank
    @Length(min = 1, max = 20)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            lei;
}
