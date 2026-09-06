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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 收款运营机构
 * @author liuqi
 * @version $Id: DbtrAgt.java, v 0.1 2019年8月24日 下午3:38:27 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "DbtrAgt")
@Getter
@Setter
@ToString
public class DbtrAgt implements Serializable{/**  */
    private static final long serialVersionUID = -825494510385779943L;
    /**
     * FinancialInstitutionIdentification
     */
    @JacksonXmlProperty(localName = "FinInstnId")
    @NotNull
    @Valid
    private FinInstnId finInstnId;

}
