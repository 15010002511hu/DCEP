/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.dto.dc642;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 
 * @author liuqi
 * @version $Id: BizQryRef.java, v 0.1 2019年8月24日 下午4:34:04 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "BizQryRef")
@Getter
@Setter
@ToString
public class BizQryRef implements Serializable {

    /**  */
    private static final long serialVersionUID = 8454184126887015718L;
    /**
     * 原查询报文标识号
     */
    @JacksonXmlProperty(localName = "QryRef")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String qryRef;

    /**
     * 原查询发起运营机构
     */
    @JacksonXmlProperty(localName = "QryNm")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 14, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String qryNm;

    /**
     * 查询处理状态
     */
    @JacksonXmlProperty(localName = "QryRs")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 4, max = 4)
    @Pattern(regexp = "PR00||PR01")
    private String qryRs;

    public BizQryRef() {

    }

    /**
     * @param qryRef
     * @param qryNm
     * @param qryRs
     */
    public BizQryRef(String qryRef, String qryNm, String qryRs) {
        this.qryRef = qryRef;
        this.qryNm = qryNm;
        this.qryRs = qryRs;
    }


}
