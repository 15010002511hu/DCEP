package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件--Envelope
 * @Author luteng
 * @date 2025-10-12 16:16:48
 */
@Data
public class Envlp implements Serializable {
    /**
     * 被调整参与机构
     */
    @JacksonXmlProperty(
            localName = "TrfdPty"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    private String trfdPty;

    /**
     * 托管行参与机构
     */
    @JacksonXmlProperty(
            localName = "Cstdn"
    )
    @Length(
            min = 1,
            max = 35
    )
    private String cstdn;

    /**
     * 资金调整类型
     */
    @JacksonXmlProperty(
            localName = "OptTp"
    )
    @Length(
            min = 1,
            max = 4
    )
    @NotBlank
    @Pattern(
            regexp = "PRFD||FFIC||TRBH||FFRD||FDRD"
    )
    private String optTp;

    /**
     * 注资最低限额
     */
    @JacksonXmlProperty(
            localName = "CILmt"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String cILmt;

    /**
     * 净额轧差保证金
     */
    @JacksonXmlProperty(
            localName = "NtQt"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String ntQt;

    /**
     * 原调减申请发起参与机构
     */
    @JacksonXmlProperty(
            localName = "OrgnlDbtReqInstgPty"
    )
    @Length(
            min = 1,
            max = 35
    )
    private String orgnlDbtReqInstgPty;

    /**
     * lDebitRequestMessageIdentification
     */
    @JacksonXmlProperty(
            localName = "OrgnlDbtReqMsgId"
    )
    @Length(
            min = 1,
            max = 35
    )
    private String orgnlDbtReqMsgId;
}
