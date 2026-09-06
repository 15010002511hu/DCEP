package com.dcep.supergw.dto.dc309;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class SubMrchntRefInf implements Serializable {
    private static final long serialVersionUID = 1422046900410051233L;
    /**
     * 二级商户编码
     */
    @JacksonXmlProperty(localName = "SubMrchntNo")
    @NotBlank
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String subMrchntNo;

    /**
     * 退款金额
     */
    @JacksonXmlProperty(localName = "RefAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount refAmt;

    /**
     * 二级商户名称
     */
    @JacksonXmlProperty(localName = "SubMrchntNm")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 60)
    private String subMrchntName;

    /**
     * 二级商户简称
     */
    @JacksonXmlProperty(localName = "SubMrchntAbbrNm")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 30)
    private String subMrchntAbbrName;

    /**
     * 商品简称列表
     */
    @JacksonXmlElementWrapper(localName = "GdAbbrList")
    @JacksonXmlProperty(localName = "GdAbbrInf")
    @Valid
    @NotNull(groups = Priority.Highest.class)
    private List<GdAbbrInf> goodsAbbrList;
}
