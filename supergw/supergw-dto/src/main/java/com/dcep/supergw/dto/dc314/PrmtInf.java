package com.dcep.supergw.dto.dc314;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : PrmtInf.java v 0.1 2022-12-29
 * Copyright 2022 PBCDCI ALL Rights
 * @description :
 */
@ToString
@Getter
@Setter
public class PrmtInf implements Serializable {
    private static final long serialVersionUID = 763463779736730028L;

    /**
     * 权益使用总金额
     */
    @NotNull
    @Valid
    @JacksonXmlProperty(localName = "CpnTtlAmt")
    ActiveCurrencyAndAmount cpnTtlAmt;

    /**
     * 权益使用总笔数
     */
    @NotBlank
    @Length(min = 1, max = 15)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "CpnTtlNb")
    String cpnTtlNb;

    /**
     * 权益信息列表--权益信息
     */
    @NotNull
    @Valid
    @JacksonXmlElementWrapper(localName = "CpnInfList")
    @JacksonXmlProperty(localName = "CpnInf")
    List<CpnInf> cpnInfList;

}
