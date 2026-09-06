package com.dcep.supergw.dto.dc415;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 收款码信息
 * @author chenkai
 */
@JacksonXmlRootElement(localName = "QRCodeInf")
@Getter
@Setter
@ToString
public class QRCodeInf implements Serializable {

    private static final long serialVersionUID = 2468026989688066226L;
    /**
     * 收款码
     */
    @JacksonXmlProperty(localName = "QrCode")
    @NotBlank
    @Length(min=1, max = 500)
    private String                  qrCode;

}
