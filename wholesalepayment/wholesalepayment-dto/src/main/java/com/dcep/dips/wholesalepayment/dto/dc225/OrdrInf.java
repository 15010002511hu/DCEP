package com.dcep.dips.wholesalepayment.dto.dc225;

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
 * 订单信息
 * 
 * @author chenxf
 *
 */
@JacksonXmlRootElement(localName = "OrdrInf")
@Setter
@Getter
@ToString
public class OrdrInf implements Serializable {

    private static final long serialVersionUID = -1657512991216690300L;

    /**
     * 订单号
     */
    @JacksonXmlProperty(localName = "OrdrNo")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String ordrNo;

    /**
     * 订单金额
     */
    @JacksonXmlProperty(localName = "OrdrAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount ordrAmt;

    /**
     * 交易类型 TT00：普通汇款 TT01：扫码支付 TT02：碰一碰支付 TT03：统一下单支付 TT04：H5拉起支付
     */
    @JacksonXmlProperty(localName = "TrxTp")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 4, max = 4)
    @Pattern(regexp = "^[T]{2}[0-9]{2}")
    private String trxTp;

    /**
     * 二维码类型 QT01：个人收款码 QT02：个人付款码 QT03：商户静态码 QT04：商户动态码
     */
    @JacksonXmlProperty(localName = "QrCodeTp")
    @Length(min = 4, max = 4)
    @Pattern(regexp = "^[Q][T][0-9]{2}")
    private String qrCodeTp;

    /**
     * 商户简称
     */
    @JacksonXmlProperty(localName = "MrchntAbbrNm")
    @Length(min = 1, max = 30)
    private String mrchntAbbrNm;

    /**
     * 商户名称
     */
    @JacksonXmlProperty(localName = "MrchntNm")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 60)
    private String mrchntNm;

    /**
     * 商户类别代码
     */
    @JacksonXmlProperty(localName = "MCC")
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mcc;

    /**
     * 商户号
     */
    @JacksonXmlProperty(localName = "MrchntNo")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mrchntNo;

    /**
     * 商品名称
     */
    @JacksonXmlProperty(localName = "GdNm")
    @Length(min = 1, max = 200)
    private String gdNm;

    /**
     * 受理机构的金融机构编码
     */
    @JacksonXmlProperty(localName = "AcqAgtInstnId")
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String acqAgtInstnId;

    /**
     * 受理机构的机构名称
     */
    @JacksonXmlProperty(localName = "AcqAgtNm")
    @Length(min = 1, max = 60)
    private String acqAgtNm;

}
