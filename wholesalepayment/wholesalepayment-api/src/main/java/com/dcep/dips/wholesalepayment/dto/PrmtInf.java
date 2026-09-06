package com.dcep.dips.wholesalepayment.dto;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.io.Serializable;
import java.util.List;

/**
 * 营销信息
 * @author caoxiaogai
 */
@JacksonXmlRootElement(localName = "PrmtInf")
@Setter
@Getter
@ToString
public class PrmtInf implements Serializable {
    private static final long serialVersionUID = -2915320423401117121L;
    /**
     * 权益使用总金额
     */
    @JacksonXmlProperty(localName = "CpnTtlAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount  cpnTtlAmt;

    /**
     * 权益使用总笔数
     */
    @JacksonXmlProperty(localName = "CpnTtlNb")
    @NotBlank
    @Length(min = 1, max = 15)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String  cpnTtlNb;

    /**
     * 权益信息列表
     */
    @NotNull(groups = Priority.Highest.class)
    @Valid
    @JacksonXmlElementWrapper(localName = "CpnInfList")
    @JacksonXmlProperty(localName = "CpnInf")
    private List<CpnInf> cpnInf;

    public PrmtInf() {
    }

   public PrmtInf(ActiveCurrencyAndAmount cpnTtlAmt, String cpnTtlNb, List<CpnInf> cpnInf){
       this.cpnTtlAmt = cpnTtlAmt;
       this.cpnTtlNb = cpnTtlNb;
       this.cpnInf = cpnInf;
   }

}
