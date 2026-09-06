package com.dcep.dips.wholesalepayment.dto.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 合约参数
 *
 * @author caoxiaogai
 *
 */
@JacksonXmlRootElement(localName = "CtrctParam")
@Setter
@Getter
@ToString
public class CtrctParam implements Serializable {

    private static final long serialVersionUID = 48850865809997042L;
    /**
     * 调用路径
     */
    @JacksonXmlProperty(localName = "InvokePth")
    @Length(min = 1, max = 128)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            invokePth;

    /**
     * 调用参数
     */
    @JacksonXmlProperty(localName = "InvokeParam")
    @Length(min = 1, max = 1024)
    private String            invokeParam;

}
