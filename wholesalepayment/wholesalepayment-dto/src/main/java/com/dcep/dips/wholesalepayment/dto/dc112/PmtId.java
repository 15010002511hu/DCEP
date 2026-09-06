package com.dcep.dips.wholesalepayment.dto.dc112;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件--PaymentIdentification
 * @Author zhaotianwu
 * @date 2025-10-13 16:56:03
 */
@Data
public class PmtId implements Serializable {
    /**
     * 指令标识号
     */
    @JacksonXmlProperty(
            localName = "InstrId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String instrId;

    /**
     * 端到端标识号
     */
    @JacksonXmlProperty(
            localName = "EndToEndId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String endToEndId;

    /**
     * 交易标识号
     */
    @JacksonXmlProperty(
            localName = "TxId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String txId;

    /**
     * 唯一标识号
     */
    @JacksonXmlProperty(
            localName = "UETR"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String uETR;
}
