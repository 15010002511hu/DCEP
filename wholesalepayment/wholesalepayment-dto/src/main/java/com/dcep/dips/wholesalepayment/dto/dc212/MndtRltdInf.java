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
 * 
 * @author liuqi
 * @version $Id: MndtRltdInf.java, v 0.1 2019年8月24日 下午3:29:29 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "MndtRltdInf")
@Getter
@Setter
@ToString
public class MndtRltdInf implements Serializable{
    /**  */
    private static final long serialVersionUID = -6750360683578647511L;

    /**
     * MandateRelatedInformation
     */
    @JacksonXmlProperty(localName = "AmdmntInfDtls")
    @NotNull
    @Valid
    private AmdmntInfDtls amdmntInfDtls;

}
