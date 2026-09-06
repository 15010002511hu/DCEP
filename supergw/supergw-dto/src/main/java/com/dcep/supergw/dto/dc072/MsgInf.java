package com.dcep.supergw.dto.dc072;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MsgInf implements Serializable {

    @NotBlank
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "MsgSndCd")
    String msgSndCd;

    @JsonFormat(locale = "zh",timezone = "GMT+8",pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotBlank(groups = Priority.Lowest.class)
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}",message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
    @JacksonXmlProperty(localName = "VerifCdValPer")
    String verifCdValPer;
}
