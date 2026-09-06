package com.dcep.supergw.dto.dc324;

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
public class RspsnInf implements Serializable {
    private static final long serialVersionUID = 110897057879976009L;
    /**
     * 业务回执状态 不能为空，码值长度4
     */
    @JacksonXmlProperty(localName = "RspsnSts")
    @NotBlank
    @Pattern(regexp = "PR[0-9]{2}")
    @Length(min = 4, max = 4)
    private String rspsnSts;

    /**
     * 业务拒绝码 标签可以不存在，存在必须有值
     */
    @JacksonXmlProperty(localName = "RjctCd")
    @Pattern(regexp = "R[0-9]{3}")
    @Length(min = 4, max = 4)
    private String rjctCd;

    /**
     * 业务拒绝信息
     */
    @JacksonXmlProperty(localName = "RjctInf")
    @Length(min = 1, max = 105)
    private String rjctInf;
    /**
     * 交易类型
     */
    @JacksonXmlProperty(localName = "TrxTp")
    @NotBlank
    @Pattern(regexp = "TT[0-9]{2}")
    private String trxTp;

    /**
     * 订单号
     */
    @JacksonXmlProperty(localName = "OrdrNo")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 64)
    private String ordrNo;

}
