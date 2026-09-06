package com.dcep.supergw.dto.dc323;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class OrdrInf implements Serializable {
    private static final long serialVersionUID = 151048944511928896L;
    /**
     * 订单号
     */
    @NotBlank
    @JacksonXmlProperty(localName = "OrdrNo")
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String ordrNo;
    /**
     * 订单时间
     */
    @NotBlank
    @JacksonXmlProperty(localName = "OrdrTm")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
    private String ordrTm;

    /**
     * 商品名称
     */
    @JacksonXmlProperty(localName = "GdNm")
    @Length(min = 1, max = 200)
    private String gdNm;
    /**
     * 订单详情
     */
    @JacksonXmlProperty(localName = "OrdrDtls")
    @Length(min = 1, max = 4096)
    private String ordrDtls;

}
