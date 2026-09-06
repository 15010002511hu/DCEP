package com.dcep.supergw.dto.dc323;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
@Getter
@Setter
@ToString
public class QrCodeInf implements Serializable {
    private static final long serialVersionUID = -346287089346483124L;
    /**
     * 二维码类型
     */
    @JacksonXmlProperty(localName = "QrCodeTp")
    @NotBlank
    @Pattern(regexp = "QT[0-9]{2}")
    private String qrCodeTp;
    /**
     * 二维码
     */
    @JacksonXmlProperty(localName = "QrCode")
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 1024)
    private String qrCode;
    /**
     * 二维码编码类型
     */
    @JacksonXmlProperty(localName = "QrEncdgTp")
    @NotBlank
    @Pattern(regexp = "QET[0-9]{2}")
    private String qrEncdgTp;
}
