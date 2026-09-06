package com.dcep.supergw.dto.dc508;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author : duzhong
 * @version : RspsnInf.java v 0.1 2020-11-23
 * @description :
 */
@Getter
@Setter
@ToString
public class RspsnInf2 implements Serializable {
    private static final long serialVersionUID = 6667063645421530029L;

    /**
     * 业务回执状态 不能为空，码值长度4
     */
    @JacksonXmlProperty(localName = "RspsnSts")
    @NotBlank
    @Pattern(regexp = "PR00||PR01||PR02||PR03||PR04")
    @Length(min = 4, max = 4)
    private String rspsnSts;

    /**
     * 业务拒绝码 
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
     * 签约协议号
     */
    @JacksonXmlProperty(localName = "PtcId")
    @Length(min = 1, max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String ptcId;
    
}
