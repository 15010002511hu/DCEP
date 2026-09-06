package com.dcep.supergw.dto.dc367;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 收款人合约应答
 *
 * @Author qinchaoyong
 * @date 2024-10-23 16:39:15
 */
@Data
public class CdtrCtrctRspsn implements Serializable {
    /**
     * 合约实例ID
     */
    @JacksonXmlProperty(
            localName = "CtrctId"
    )
    @Length(
            min = 1,
            max = 64
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String ctrctId;

    /**
     * 合约退款金额
     */
    @JacksonXmlProperty(
            localName = "CtrctTxAmt"
    )
    @NotNull
    @Valid
    private ActiveCurrencyAndAmount ctrctTxAmt;

    /**
     * 合约返回码
     */
    @JacksonXmlProperty(
            localName = "CtrctPrcCd"
    )
    @NotBlank
    @Length(
            min = 1,
            max = 8
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String ctrctPrcCd;

    /**
     * 合约返回信息
     */
    @JacksonXmlProperty(
            localName = "CtrctPrcInf"
    )
    private String ctrctPrcInf;
}
