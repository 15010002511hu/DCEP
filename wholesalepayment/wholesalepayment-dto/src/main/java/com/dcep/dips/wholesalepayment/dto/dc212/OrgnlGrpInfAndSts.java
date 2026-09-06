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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 原报文信息
 * @author chenkai
 * @version $Id: OrgnlGrpHdr.java, v 0.1 2019年8月24日 下午2:28:31 chenkai Exp $
 */

@JacksonXmlRootElement(localName = "OrgnlGrpInfAndSts")
@Getter
@Setter
@ToString
public class OrgnlGrpInfAndSts implements Serializable {
    /**  */
    private static final long serialVersionUID = 5143012740102457314L;

    /**
     * 【原报文标识号】
     */
    @JacksonXmlProperty(localName = "OrgnlMsgId")
    @NotBlank
    @Length(min = 32, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            orgnlMsgId;

    /**
     * 【原报文編号】
     */
    @JacksonXmlProperty(localName = "OrgnlMsgNmId")
    @NotBlank
    @Length(min = 15, max = 15)
    private String            orgnlMsgNmId;

    /**
     * 【处理信息】
     */
    @JacksonXmlProperty(localName = "StsRsnInf")
    @Valid
    private StsRsnInf stsRsnInf;
}
