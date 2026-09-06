package com.dcep.supergw.dto.dc302;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @Author weiqianjing
 * @Date 2021/12/6 15:55
 * @Description
 */
@Setter
@Getter
@ToString
public class PushSubWltInf implements Serializable {


    private static final long serialVersionUID = -3068583362737207127L;
    /**
     * 子钱包商户号
     */
    @Length(min = 1, max = 64)
    @JacksonXmlProperty(localName = "SubMrchntNo")
    @NotBlank
    private String subMrchntNo;

    /**
     * 手机号
     */
    @Length(min = 1, max = 35)
    @JacksonXmlProperty(localName = "MblPhNo")
    @NotBlank
    private String mblPhNo;

    /**
     * 商户侧账号
     */
    @Length(min = 1, max = 256)
    @JacksonXmlProperty(localName = "MrchntAcctId")
    @NotBlank
    private String mrchntAcctId;

    /**
     * 商户授权码
     */
    @Length(min = 1, max = 128)
    @JacksonXmlProperty(localName = "VeryCd")
    @NotBlank
    private String veryCd;
}
