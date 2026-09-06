/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc202;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckClrMsgId;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author liuqi
 * @version $Id: GrpHdr.java, v 0.1 2019年8月24日 下午2:24:20 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "GrpHdr")
@Getter
@Setter
@ToString
public class GrpHdr implements Serializable {
    /**  */
    private static final long serialVersionUID = -3742363043678860618L;
    /**
     * 【报文标识号】
     */
    @JacksonXmlProperty(localName = "MsgId")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 32, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @CheckClrMsgId
    private String            msgId;
    /**
     * 报文发送时间
     */

    @JacksonXmlProperty(localName = "CreDtTm")
    @NotBlank(groups = Priority.Highest.class)
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
    private String            creDtTm;

}
