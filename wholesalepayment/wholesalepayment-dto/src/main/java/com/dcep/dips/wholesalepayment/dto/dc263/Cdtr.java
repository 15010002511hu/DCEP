/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc263;

import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckOrgState;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author chenxf
 * @version $Id: Cdtr.java
 */
@JacksonXmlRootElement(localName = "Cdtr")
@Setter
@Getter
@ToString
public class Cdtr implements Serializable {
	/**  */
	private static final long serialVersionUID = -4373142099341657464L;

	/**
	 * 收款运营机构
	 */
	@JacksonXmlProperty(localName = "CdtrPtyId")
	@Length(min = 14, max = 14)
	@Pattern(regexp = "^[0-9a-zA-Z]{14}$")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@CheckOrgState(groups = Priority.Lowest.class)
	private String cdtrPtyId;

}
