package com.dcep.dips.wholesalepayment.dto.dc281;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 商品简称信息
 *
 * @author chenxingfeng
 *
 */
@JacksonXmlRootElement(localName = "GdAbbrInf")
@Setter
@Getter
@ToString
public class GdAbbrInf implements Serializable {

    private static final long serialVersionUID = -6998335654622310118L;
    /**
     * 商品简称
     */
    @JacksonXmlProperty(localName = "GdAbbrNm")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 20)
    private String            gdAbbrNm;
    
}
