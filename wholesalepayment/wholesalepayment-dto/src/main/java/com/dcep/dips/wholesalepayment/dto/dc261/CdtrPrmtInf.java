package com.dcep.dips.wholesalepayment.dto.dc261;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
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

/**
 * @author chenxingfeng
 * @date 2025年1月15日 下午7:21:22
 * @Description:CdtrPrmtInf.java
 */
@JacksonXmlRootElement(localName = "CdtrPrmtInf")
@Getter
@Setter
@ToString
public class CdtrPrmtInf implements Serializable {

    private static final long serialVersionUID = 1390498135150425136L;
    
    /**
     * 收款方优惠活动流水号
     */
    @JacksonXmlProperty(localName = "PrmtSrlNo")
    @Length(min = 1, max = 64)
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String prmtSrlNo;

    /**
     * 订单金额
     */
    @JacksonXmlProperty(localName = "OrdrAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount ordrAmt;

    /**
     * 优惠后应收金额
     */
    @JacksonXmlProperty(localName = "RcvblAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount rcvblAmt;

    /**
     * 总优惠金额
     */
    @JacksonXmlProperty(localName = "TtlCpnAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount ttlCpnAmt;

}
