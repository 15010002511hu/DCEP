package com.dcep.dips.wholesalepayment.dto.dc263;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 收款人合约实例信息
 *
 * @author caoxiaogai
 *
 */
@JacksonXmlRootElement(localName = "CdtrCtrctInst")
@Setter
@Getter
@ToString
public class CdtrCtrctInst implements Serializable {

    private static final long serialVersionUID = 4885086580999704218L;
    /**
     * 合约实例ID
     */
    @JacksonXmlProperty(localName = "CtrctId")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @NotBlank
    private String ctrctId;

    /**
     * 合约产品ID
     */
    @JacksonXmlProperty(localName = "PdctId")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String pdctId;

}
