package com.dcep.dips.wholesalepayment.dto;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 权益信息
 * @author caoxiaogai
 */
@JacksonXmlRootElement(localName = "CpnInf")
@Setter
@Getter
@ToString
public class CpnInf  implements Serializable {
    private static final long serialVersionUID = -2915320423401117121L;
    /**
     * 营销活动运营机构
     */
    @JacksonXmlProperty(localName = "PrmtGnrtdInstnId")
    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String  prmtGnrtdInstnId;

    /**
     * 营销活动编码
     */
    @JacksonXmlProperty(localName = "PrmtId")
    @NotBlank
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String  prmtId;

    /**
     * 营销活动名称
     */
    @JacksonXmlProperty(localName = "PrmtNm")
    @NotBlank
    @Length(min = 1, max = 60)
    private String  prmtNm;

    /**
     * 营销活动描述信息
     */
    @JacksonXmlProperty(localName = "PrmtDescInf")
    @Length(min = 1, max = 256)
    private String  prmtDescInf;

    /**
     * 营销活动类型
     */
    @JacksonXmlProperty(localName = "PrmtTp")
    @Pattern(regexp = "^[P][T][0-9]{2}")
    private String  prmtTp;

    /**
     * 权益ID
     */
    @JacksonXmlProperty(localName = "CpnId")
    @NotBlank
    @Length(min = 1, max = 64)
    private String  cpnId;

    /**
     * 权益有效期
     */
    @JacksonXmlProperty(localName = "CpnExp")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
    private String  cpnExp;

    /**
     * 权益金额
     */
    @JacksonXmlProperty(localName = "CpnAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount  cpnAmt;

    /**
     * 权益退款属性
     */
    @JacksonXmlProperty(localName = "CpnRefPrprty")
    @Pattern(regexp = "^[C][R][P][0-9]{2}")
    private String  cpnRefPrprty;

    /**
     * 权益描述信息
     */
    @JacksonXmlProperty(localName = "CpnDescInf")
    @Length(min = 1, max = 256)
    private String  cpnDescInf;

    /**
     * 备注信息
     */
    @JacksonXmlProperty(localName = "RmkInf")
    @Length(min = 1, max = 128)
    private String  rmkInf;

    public CpnInf() {
    }
    public CpnInf(String prmtGnrtdInstnId, String prmtId, String prmtNm, String prmtDescInf, String prmtTp, String cpnId,
                  String cpnExp, ActiveCurrencyAndAmount cpnAmt, String cpnRefPrprty, String cpnDescInf, String rmkInf) {
        this.prmtGnrtdInstnId = prmtGnrtdInstnId;
        this.prmtId = prmtId;
        this.prmtNm = prmtNm;
        this.prmtDescInf = prmtDescInf;
        this.prmtTp = prmtTp;
        this.cpnId = cpnId;
        this.cpnExp = cpnExp;
        this.cpnAmt = cpnAmt;
        this.cpnRefPrprty = cpnRefPrprty;
        this.cpnDescInf = cpnDescInf;
        this.rmkInf = rmkInf;
    }
}
