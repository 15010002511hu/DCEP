package com.dcep.supergw.dto.dc075;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SyncFileInf implements Serializable {

    @NotBlank
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd")
    @JacksonXmlProperty(localName = "SyncDt")
    String syncDt;

    @NotBlank
    @Length(min = 1,max = 15)
    @Pattern(regexp = "^\\d+$", message = "总比数只能是数字")
    @JacksonXmlProperty(localName = "CntNb")
    String cntNb;

}
