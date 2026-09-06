/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc21302;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author cxg
 * @version $Id: Prxy.java, v 0.1
 */
@JacksonXmlRootElement(localName = "Prxy")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Prxy implements Serializable {
    /**  */
    private static final long serialVersionUID = 6218072563831051366L;
    /**
     * 链上账户地址
     */
    @JacksonXmlProperty(localName = "Id")
    @NotBlank
    @Length(min = 0, max = 2048)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String id;
}
