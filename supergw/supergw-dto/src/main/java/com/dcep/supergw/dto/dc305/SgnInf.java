package com.dcep.supergw.dto.dc305;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @Author weiqianjing
 * @Date 2021/9/26 17:03
 * @Description
 */
@Setter
@Getter
@ToString
public class SgnInf implements Serializable {
    private static final long serialVersionUID = -6431201816586630959L;
    /**
     * 签约协议号
     */
    @JacksonXmlProperty(localName = "PtcId")
    @NotBlank
    @Length(min = 1, max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String ptcId;
}
