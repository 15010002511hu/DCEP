package com.dcep.dips.wholesalepayment.dto.dc212;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 收款人钱包合约应答
 *
 * @author caoxiaogai
 *
 */
@JacksonXmlRootElement(localName = "CtrctRspsn")
@Setter
@Getter
@ToString
public class CtrctRspsn implements Serializable {

    private static final long serialVersionUID = 48850865809997042L;
    /**
     * 合约实例ID
     */
    @JacksonXmlProperty(localName = "CtrctId")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @NotBlank
    private String ctrctId;

    /**
     * 合约产品ID
     */
    @JacksonXmlProperty(localName = "PdctId")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String pdctId;

    /**
     * 合约交易金额
     */
    @JacksonXmlProperty(localName = "CtrctTxAmt")
    @NotNull
    private ActiveCurrencyAndAmount ctrctTxAmt;

    /**
     * 合约返回码
     */
    @JacksonXmlProperty(localName = "CtrctPrcCd")
    @Length(min = 1, max = 8)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @NotBlank
    private String ctrctPrcCd;

    /**
     * 合约返回信息
     */
    @JacksonXmlProperty(localName = "CtrctPrcInf")
    @Length(min = 1, max = 20480)
    private String ctrctPrcInf;

}
