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
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author liuqi
 * @version $Id: DC.java, v 0.1 2019年8月23日 下午8:02:34 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "DC")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DC implements Serializable {/**  */
    private static final long serialVersionUID = 3700755806432249334L;
    /*
     * 数字货币
     */
    @JacksonXmlProperty(localName = "Id")
    @NotBlank
    @Length(min = 1, max = 2048)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            id;

}
