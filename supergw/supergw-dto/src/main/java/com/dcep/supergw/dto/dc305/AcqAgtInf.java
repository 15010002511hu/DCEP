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
 * @Date 2021/9/26 15:38
 * @Description
 */
@Setter
@Getter
@ToString
public class AcqAgtInf implements Serializable {

    private static final long serialVersionUID = -8240818643672241516L;
    /**
     * 受理机构金融编码
     */
    @JacksonXmlProperty(localName = "AcqAgtInstnId")
    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String acqAgtInstnId;
    /**
     * 受理机构名称
     */
    @JacksonXmlProperty(localName = "AcqAgtNm")
    @NotBlank
    @Length(min = 1, max = 60)
    private String acqAgtNm;
}
