package com.dcep.supergw.dto.dc302;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 调用信息
 *
 * @author duzhong
 */
@Getter
@Setter
@ToString
public class InvokeInf implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1105194311543106582L;

    /**
     *
     */
    @JacksonXmlProperty(localName = "InvokeUrl")
    @Length(min = 1, max = 256)
    private String invokeUrl;

    /**
     * 加密信息
     */
    @JacksonXmlProperty(localName = "EncInf")
    @Valid
    private String encInf;

}
