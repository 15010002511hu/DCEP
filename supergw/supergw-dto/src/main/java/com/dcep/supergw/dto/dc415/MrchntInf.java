package com.dcep.supergw.dto.dc415;

import com.dcep.common.model.ActiveCurrencyAndAmount;
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
 * 收款码信息
 * @author chenkai
 */
@JacksonXmlRootElement(localName = "MrchntInf")
@Getter
@Setter
@ToString
public class MrchntInf implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -8899341836579190684L;
    /**
     	*商户号
     */
	@JacksonXmlProperty(localName = "MrchntNo")
    @Length(min = 1,max = 35)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@NotBlank
	private String mrchntNo;
    /**
 		*订单号
     */
	@JacksonXmlProperty(localName = "OrdrNo")
    @Length(min = 1,max = 40)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@NotBlank
	private String ordrNo;
    /**
     	*订单金额
     */
    @JacksonXmlProperty(localName = "OrdrAmt")
    @Valid
    @NotNull
    private ActiveCurrencyAndAmount ordrAmt;
	
}
