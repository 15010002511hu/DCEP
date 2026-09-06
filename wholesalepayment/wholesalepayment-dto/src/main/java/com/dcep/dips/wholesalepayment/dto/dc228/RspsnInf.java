/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc228;

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
 * @author lizhiguo
 * @version $Id: Dcep22700101.java, v 0.1 20200427 lizhiguo Exp $
 */
@JacksonXmlRootElement(localName = "RspsnInf")
@Setter
@Getter
@ToString
public class RspsnInf implements Serializable {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 2409419777646562171L;

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
     * 居民类型
     */
    @JacksonXmlProperty(localName = "ResdtTp")
    @Pattern(regexp = "^[R][T][0-9]{2}||REST[0-9]{2}")
    private String resdtTp;

    /**
     * 常驻国家/地区代码
     */
    @JacksonXmlProperty(localName = "ResdtCtryCd")
    @Length(min = 1, max = 3)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String resdtCtryCd;

    /**
     * 钱包注册手机号所在国家/地区代码
     */
    @JacksonXmlProperty(localName = "RegrCtryCd")
    @Length(min = 1, max = 3)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String regrCtryCd;
}
