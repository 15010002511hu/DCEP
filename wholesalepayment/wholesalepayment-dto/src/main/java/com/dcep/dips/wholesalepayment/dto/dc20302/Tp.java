/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc20302;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author liuqi
 * @version $Id: Tp.java, v 0.1 2019年8月23日 下午8:00:33 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "Tp")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Tp implements Serializable {
    /**  */
    private static final long serialVersionUID = 6218072563831051366L;
    /**
     * 付款人钱包类型/收款人钱包类型
     */
    @JacksonXmlProperty(localName = "Prtry")
    @NotBlank
    @Pattern(regexp = "WT01||WT02||WT09||WT10")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            prtry;
}
