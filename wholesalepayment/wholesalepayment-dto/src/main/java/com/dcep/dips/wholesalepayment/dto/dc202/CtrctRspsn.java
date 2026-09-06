package com.dcep.dips.wholesalepayment.dto.dc202;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 收款人钱包合约应答
 *
 * @author caoxiaogai
 *
 */
@JacksonXmlRootElement(localName = "CtrctRspsn")
@Setter
@Getter
@ToString
public class CtrctRspsn implements Serializable {

    private static final long serialVersionUID = 48850865809997042L;
    /**
     * 合约返回码
     */
    @JacksonXmlProperty(localName = "CtrctPrcCd")
    @Length(min = 1, max = 8)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @NotBlank
    private String ctrctPrcCd;

    /**
     * 合约返回信息
     */
    @JacksonXmlProperty(localName = "CtrctPrcInf")
    @Length(min = 1, max = 20480)
    private String ctrctPrcInf;

}
