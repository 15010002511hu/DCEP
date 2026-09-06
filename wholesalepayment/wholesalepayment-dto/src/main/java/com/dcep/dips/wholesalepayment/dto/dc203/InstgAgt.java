/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc203;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 
 * @author liuqi
 * @version $Id: InstgAgt.java, v 0.1 2019年8月23日 下午7:40:19 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "InstgAgt")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InstgAgt implements Serializable {
    /**  */
    private static final long serialVersionUID = 6807948631590008996L;

    /**
     * FinancialInstitutionIdentification
     */
    @JacksonXmlProperty(localName = "FinInstnId")
    @Valid
    @NotNull
    private FinInstnId        finInstnId;
    
    /**
     * 付款机构
     */
    @JacksonXmlProperty(localName = "BrnchId")
    @Valid
    @NotNull
    private BrnchId brnchId;
}
