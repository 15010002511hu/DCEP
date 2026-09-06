package com.dcep.dips.wholesalepayment.dto.dc211;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JacksonXmlRootElement(localName = "LclInstrm")
@Setter
@Getter
@ToString
public class LclInstrm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5464073578307591519L;
	 /**
     * 业务类型编码
     */
    @JacksonXmlProperty(localName = "Prtry")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 3, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  prtry;

}
