package com.dcep.supergw.dto.dc323;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
@Getter
@Setter
@ToString
public class UserTerInf implements Serializable {
    private static final long serialVersionUID = 8343102832780310926L;
    /**
     * 交易设备信息
     */
    @JacksonXmlProperty(localName = "TrxDevcInf")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 149)
    private String trxDevcInf;
}
