package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 参考信息
 * @Author luteng
 * @date 2025-10-12 16:16:53
 */
@Data
public class Refs implements Serializable {
    /**
     * 原报文标识号
     */
    @JacksonXmlProperty(
            localName = "MsgId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String msgId;

    /**
     * 原业务发起直接参与者行号
     */
    @JacksonXmlProperty(
            localName = "AcctSvcrRef"
    )
    @Length(
            min = 1,
            max = 35
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String acctSvcrRef;

    @JacksonXmlProperty(
            localName = "Prtry"
    )
    @NotNull
    @Valid
    private PrtryOfTp prtry;
}
