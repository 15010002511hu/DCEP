/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc309;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 商品简称信息
 *
 * @author qinchaoyong
 * @date 2023/6/28 17:52
 */
@Data
public class GdAbbrInf {

    /**
     * 商品简称
     */
    @JacksonXmlProperty(localName = "GdAbbrNm")
    @NotNull(groups = Priority.Highest.class)
    @Length(min = 1, max = 20)
    @Valid
    private String goodsAbbrName;
}
