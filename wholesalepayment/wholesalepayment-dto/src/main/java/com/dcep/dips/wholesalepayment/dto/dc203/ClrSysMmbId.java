/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc203;

import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckOrgState;
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
 * @version $Id: ClrSysMmbId.java, v 0.1 2019年8月23日 下午7:44:21 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "ClrSysMmbId")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClrSysMmbId implements Serializable {
    /**  */
    private static final long serialVersionUID = 1304282182865008514L;
    /**
     * 付款运营机构/收款运营机构
     */
    @JacksonXmlProperty(localName = "MmbId")
    @NotBlank
    @Length(min = 14, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @CheckOrgState(groups = Priority.Lowest.class)
    private String            mmbId;
}
