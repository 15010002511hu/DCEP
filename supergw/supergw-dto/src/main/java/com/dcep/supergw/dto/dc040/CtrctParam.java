/*
 * pbcdci.cn Inc. Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc040;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class CtrctParam implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 调用路径
     */
    @JacksonXmlProperty(localName = "CallPth")
    @Length(min = 1, max = 128)
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String callPth;

    /**
     * 调用参数
     */
    @JacksonXmlProperty(localName = "CallParam")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String callParam;

}
