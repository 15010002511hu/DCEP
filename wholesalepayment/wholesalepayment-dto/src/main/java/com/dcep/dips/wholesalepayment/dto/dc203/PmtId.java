/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc203;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author liuqi
 * @version $Id: PmtId.java, v 0.1 2019年8月23日 下午7:24:09 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "PmtId")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PmtId implements Serializable {

    /**  */
    private static final long serialVersionUID = 6327458056967646414L;

    /**
     * 交易批次号
     */
    @JacksonXmlProperty(localName = "InstrId")
    @NotBlank
    @Pattern(regexp = "^[B][0-9]{12}$")
    private String            instrId;

    /**
     * 端到端标识号
     */
    @JacksonXmlProperty(localName = "EndToEndId")
    @NotBlank
    @Length(min = 1, max = 35)
    private String            endToEndId;

    /**
     * 明细标识号 同报文标识号
     */
    @JacksonXmlProperty(localName = "TxId")
    @NotBlank
    @Length(min = 32, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            txId;


}
