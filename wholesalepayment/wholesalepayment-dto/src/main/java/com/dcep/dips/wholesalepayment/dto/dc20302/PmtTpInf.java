/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc20302;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 
 * @author liuqi
 * @version $Id: PmtTpInf.java, v 0.1 2019年8月23日 下午7:29:52 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "PmtTpInf")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PmtTpInf implements Serializable {
    /**  */
    private static final long serialVersionUID = -731918843538048141L;

    /**
     * Proprietary
     */
    @JacksonXmlProperty(localName = "SvcLvl")
    @NotNull
    @Valid
    private SvcLvl svcLvl;

    /**
     * CategoryPurpose
     */
    @JacksonXmlProperty(localName = "CtgyPurp")
    @NotNull
    @Valid
    private CtgyPurp ctgyPurp;

}
