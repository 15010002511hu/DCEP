/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc20302;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author liuqi
 * @version $Id: Purp.java, v 0.1 2019年8月23日 下午8:12:05 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "Purp")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Purp implements Serializable {
    /**  */
    private static final long serialVersionUID = 2014079329790129200L;
    /**
     * 业务种类编码
     */
    @JacksonXmlProperty(localName = "Prtry")
    @NotBlank
    @Pattern(regexp = "^[0-9]{8}$", message = "业务种类编码仅允许8位数字")
    private String            prtry;
}
