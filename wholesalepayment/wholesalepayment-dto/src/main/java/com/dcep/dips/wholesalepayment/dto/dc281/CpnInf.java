package com.dcep.dips.wholesalepayment.dto.dc281;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 退款权益信息
 * @author caoxiaogai
 */
@JacksonXmlRootElement(localName = "CpnInf")
@Setter
@Getter
@ToString
public class CpnInf implements Serializable {
    private static final long serialVersionUID = -2915320423401117121L;


    /**
     * 原营销活动编码
     */
    @JacksonXmlProperty(localName = "PrmtId")
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String  prmtId;

    /**
     * 原权益ID
     */
    @JacksonXmlProperty(localName = "CpnId")
    @Length(min = 1, max = 64)
    private String  cpnId;

    /**
     * 原权益退款金额
     */
    @JacksonXmlProperty(localName = "CpnAmt")
    @Valid
    private ActiveCurrencyAndAmount  cpnAmt;

    public CpnInf() {
    }
}
