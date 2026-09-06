package com.dcep.supergw.dto.dc326;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
@Setter
@Getter
@ToString
public class BizQryRef implements Serializable {
    private static final long serialVersionUID = 1767686363188718320L;
    /**
     * 原查询报文标识号
     */
    @JacksonXmlProperty(localName = "QryRef")
    @NotBlank
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String qryRef;

    /**
     * 原查询发起运营机构
     */
    @JacksonXmlProperty(localName = "QryNm")
    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String qryNm;

    /**
     * 查询处理状态
     */
    @JacksonXmlProperty(localName = "QryRs")
    @NotBlank
    @Length(min = 1, max = 4)
    @Pattern(regexp = "PR[0-9]{2}")
    private String qryRs;
}
