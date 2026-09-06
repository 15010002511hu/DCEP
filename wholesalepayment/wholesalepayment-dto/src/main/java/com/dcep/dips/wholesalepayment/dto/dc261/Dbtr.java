/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc261;

import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckOrgState;
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
 * @author chenxf
 * @version $Id: Dbtr.java
 */
@JacksonXmlRootElement(localName = "Dbtr")
@Setter
@Getter
@ToString
public class Dbtr implements Serializable {
	/**  */
	private static final long serialVersionUID = 5116147351803361659L;
	/**
	 * 用户唯一标识，由付款运营机构或者App前端生成确保唯一
	 */
	@JacksonXmlProperty(localName = "UserUniqId")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 128)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String userUniqId;
	
	/**
     * 用户手机号标识，由付款运营机构或者App前端生成确保唯一
     */
    @JacksonXmlProperty(localName = "PhneNbHash")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String phneNbHash;

	/**
	 * 付款运营机构
	 */
	@JacksonXmlProperty(localName = "DbtrPtyId")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 14, max = 14)
	@Pattern(regexp = "^[0-9a-zA-Z]{14}$")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@CheckOrgState(groups = Priority.Lowest.class)
	private String dbtrPtyId;

}
