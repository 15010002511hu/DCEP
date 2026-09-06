package com.dcep.dips.wholesalepayment.dto.dc213;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author sunxiaofeng
 *
 */
@JacksonXmlRootElement(localName = "RmtInf")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RmtInf implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8080193621631960466L;

    @JacksonXmlProperty(localName = "Postscript")
    @Length(min = 1, max = 140)
    private String postscript;

    @JacksonXmlProperty(localName = "Remark")
    @Length(min = 1, max = 120)
    private String remark;

    /**
     * 认证方式 /AuthCode/Value AC00：协议方式 AC01：在线认证方式 AC02：动态密码方式 AC03：短信认证方式
     */
    @JacksonXmlProperty(localName = "AuthCode")
    @Length(min = 4, max = 4)
    @Pattern(regexp = "^[A][C][0-9]{2}||VM00")
    private String authCode;

    @JacksonXmlProperty(localName = "AuthInfo")
    @Length(min = 1, max = 130)
    private String authInfo;

    /*
     * 申请理由
     */
    @JacksonXmlProperty(localName = "Reason")
    @Length(min = 1, max = 70)
    private String reason;

    /*
     * 发行和注销方式编码
     */
    @JacksonXmlProperty(localName = "ParameterId")
    @Length(min = 1, max = 32)
    private String parameterId;

    /*
     * 钱包地址
     */
    @JacksonXmlProperty(localName = "WltAddr")
    @Length(min = 1, max = 40)
    private String wltAddr;

    /*
     * 发起方系统标识
     */
    @JacksonXmlProperty(localName = "SndChnlSys")
    @Length(min = 1, max = 8)
    @Pattern(regexp = "DCEP||MCBS||GCSC")
    private String sndChnlSys;

    /*
     * 接收方系统标识
     */
    @JacksonXmlProperty(localName = "RcvChnlSys")
    @Length(min = 1, max = 8)
    @Pattern(regexp = "DCEP||MCBS||GCSC")
    private String rcvChnlSys;

    /**
     * 通过List构造RmtInf对象，不含标签
     * 
     * @param ustrds
     */
    public RmtInf(List<String> ustrds) {
        for (String ustrd : ustrds) {
            if (ustrd.contains("/Postscript/")) {
                this.setPostscript(ustrd.substring("/Postscript/".length()));
            } else if (ustrd.contains("/Remark/")) {
                this.setRemark(ustrd.substring("/Remark/".length()));
            } else if (ustrd.contains("/AuthCode/")) {
                this.setAuthCode(ustrd.substring("/AuthCode/".length()));
            } else if (ustrd.contains("/AuthInfo/")) {
                this.setAuthInfo(ustrd.substring("/AuthInfo/".length()));
            } else if (ustrd.contains("/Reason/")) {
                this.setReason(ustrd.substring("/Reason/".length()));
            } else if (ustrd.contains("/ParameterId/")) {
                this.setParameterId(ustrd.substring("/ParameterId/".length()));
            } else if (ustrd.contains("/WltAddr/")) {
                this.setWltAddr(ustrd.substring("/WltAddr/".length()));
            } else if (ustrd.contains("/SndChnlSys/")) {
                this.setSndChnlSys(ustrd.substring("/SndChnlSys/".length()));
            } else if (ustrd.contains("/RcvChnlSys/")) {
                this.setRcvChnlSys(ustrd.substring("/RcvChnlSys/".length()));
            }
        }
    }

}
