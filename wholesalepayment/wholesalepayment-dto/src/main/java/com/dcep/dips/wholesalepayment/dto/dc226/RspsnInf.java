/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc226;

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
 * @author liuqi
 * @version $Id: RspsnInf.java, v 0.1 2019年8月22日 下午7:38:16 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "RspsnInf")
@Setter
@Getter
@ToString
public class RspsnInf implements Serializable {
    /**  */
    private static final long serialVersionUID = 5563973756427394465L;
    /**
     * 业务状态
     */
    @JacksonXmlProperty(localName = "PrcSts")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 4, max = 4)
    @Pattern(regexp = "^[P][R][0-9]{2}")
    private String            prcSts;

    /**
     * 业务回执状态
     */
    @JacksonXmlProperty(localName = "RspsnSts")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 4, max = 4)
    @Pattern(regexp = "PR00||PR01")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            rspsnSts;

    /**
     * 业务拒绝码
     */
    @JacksonXmlProperty(localName = "RjctCd")
    @Pattern(regexp = "^[R][0-9]{3}$")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            rjctCd;

    /**
     * 业务拒绝信息
     */
    @JacksonXmlProperty(localName = "RjctInf")
    @Length(min = 1, max = 105)
    private String            rjctInf;

    /**
     * 交易批次号
     */
    @JacksonXmlProperty(localName = "BatchId")
    @Pattern(regexp = "^[B][0-9]{12}$")
    private String            batchId;

    /**
     * SysWorkDt 系统工作日期
     */
    @JacksonXmlProperty(localName = "SysWorkDt")
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd")
    private String                  sysWorkDt;

    /**
     * SttlmDt 结算日期
     */
    @JacksonXmlProperty(localName = "SttlmDt")
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd")
    private String                  sttlmDt;
}
