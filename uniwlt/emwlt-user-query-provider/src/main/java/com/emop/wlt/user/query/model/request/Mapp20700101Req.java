package com.emop.wlt.user.query.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

/**
 * 发送协议至电子邮箱 请求
 */

@Data
@ToString
public class Mapp20700101Req {

    /**
     * 账户手机号
     */
    private String mobileNumber;

    /**
     * 邮箱地址
     */
    @NotBlank
    private String email;

    /**
     * 语言
     */
    @NotBlank
    @Pattern(regexp = "zh-CN|zh-HK|en-US|pt-PT")
    private String language;

    /**
     * 协议号
     */
    @NotBlank
    private String protocolNo;
}
