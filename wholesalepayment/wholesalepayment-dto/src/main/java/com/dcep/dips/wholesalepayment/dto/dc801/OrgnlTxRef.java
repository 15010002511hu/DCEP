package com.dcep.dips.wholesalepayment.dto.dc801;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 原交易信息组件OrgnlTxRef
 * @author chenxf
 * @version $Id: OrgnlGrpHdr.java, v 0.1 2019年12月6日 上午9:08:07  chenxf  Exp $
 */
@JacksonXmlRootElement(localName = "OrgnlTxRef")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrgnlTxRef implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
    /**
	 * 原交易金额
	 */
	@JacksonXmlProperty(localName = "OrgnlTxAmt")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ActiveCurrencyAndAmount orgnlTxAmt;

}
