/*
 * pbcdci.cn Inc. Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc642;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 应答的原业务信息
 * 
 * @author laimincai
 * @date 2024/04/24
 */
@Getter
@Setter
@ToString
public class BizRpt implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6782381811334498533L;


    /**
     * 是否需要支付密码验证
     */
    @JacksonXmlProperty(localName = "PwdVrfyFlg")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 5)
    @Pattern(regexp = "YES||NO")
    private String pwdVrfyFlg;

    /**
     * 授权验证码
     */
    @JacksonXmlProperty(localName = "VrfyAuthCd")
    @Length(min = 1, max = 64)
    private String vrfyAuthCd;

    /**
     * 钱包信息
     */
    @JacksonXmlProperty(localName = "WltInf")
    @Valid
    private WltInf wltInf;

}
