package com.dcep.supergw.dto.dc305;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @Author weiqianjing
 * @Date 2021/9/26 15:37
 * @Description
 */
@JacksonXmlRootElement(localName = "TrxInf")
@Setter
@Getter
@ToString
public class TrxInf implements Serializable {

    private static final long serialVersionUID = 7677928169943292749L;
    /**
     * 交易类型
     */
    @JacksonXmlProperty(localName = "TrxTp")
    @NotBlank
    @Pattern(regexp = "TT[0-9]{2}")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String trxTp;

    /**
     * 交易金额
     */
    @JacksonXmlProperty(localName = "TrxAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount trxAmt;

    /**
     * 业务类型编码
     */
    @JacksonXmlProperty(localName = "TrxBizTp")
    @NotBlank
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String trxBizTp;
    /**
     * 业务种类编码
     */
    @JacksonXmlProperty(localName = "TrxCtgyCd")
    @NotBlank
    @Length(min = 1, max = 8)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String trxCtgyCd;
}
