package com.dcep.supergw.dto.dc059;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 发起方合约实例
 *
 * @Author qinchaoyong
 * @date 2024-11-22 09:26:25
 */
@Data
public class SndrCtrctInst implements Serializable {
    /**
     * 合约实例ID
     */
    @JacksonXmlProperty(
            localName = "SndrCtrctInstId"
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
    private String sndrCtrctInstId;

    /**
     * 合约产品ID
     */
    @JacksonXmlProperty(
            localName = "SndrPdctId"
    )
    @Length(
            min = 1,
            max = 16
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String sndrPdctId;

    /**
     * 金额
     */
    @JacksonXmlProperty(
            localName = "Amt"
    )
    @Valid
    private ActiveCurrencyAndAmount amt;
}
